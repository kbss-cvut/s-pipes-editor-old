'use strict';

import {Configuration, WizardGenerator} from 'semforms';
import Actions from '../../../actions/Actions';
import Ajax from '../../../utils/Ajax';
import FormGenStore from '../../../stores/FormGenStore';
import I18nStore from '../../../stores/I18nStore';
import Logger from '../../../utils/Logger';
import TypeaheadResultList from '../../typeahead/TypeaheadResultList';
import WizardStore from '../../../stores/WizardStore';

const FORM_GEN_URL = 'rest/scripts/';
const QUESTION_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/question-dto";
const MODULE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-module-uri";
const MODULE_TYPE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes-view/has-module-type-uri";

export default class WizardBuilder {

    static generateWizard(script, module, moduleType, record, renderCallback) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_TYPE_URI] = moduleType;
        request[MODULE_URI] = module;
        Ajax.post(FORM_GEN_URL + script + "/forms", JSON.stringify(request)).end((data) => {
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