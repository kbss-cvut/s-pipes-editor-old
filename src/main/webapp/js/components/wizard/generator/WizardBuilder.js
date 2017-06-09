'use strict';

import {Configuration, WizardGenerator} from "semforms";
import Actions from "../../../actions/Actions";
import Ajax from "../../../utils/Ajax";
import FormGenStore from "../../../stores/FormGenStore";
import I18nStore from "../../../stores/I18nStore";
import Logger from "../../../utils/Logger";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import WizardStore from "../../../stores/WizardStore";

const FORM_GEN_URL = 'rest/nodes/6/form';

export default class WizardBuilder {

    static generateWizard(record, renderCallback) {
        Ajax.post(FORM_GEN_URL, record).end((data) => {
            Configuration.actions = Actions;
            Configuration.wizardStore = WizardStore;
            Configuration.optionsStore = FormGenStore;
            Configuration.intl = I18nStore.getIntl();
            Configuration.typeaheadResultList = TypeaheadResultList;
            WizardGenerator.createWizard(data, record.question, null, renderCallback);
        }, () => {
            Logger.error('Received no valid wizard.');
        });
    }
};
