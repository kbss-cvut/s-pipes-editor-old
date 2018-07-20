'use strict';

import {Configuration, WizardGenerator} from 's-forms';
import Actions from '../../../actions/Actions';
import Ajax from '../../../utils/Ajax';
import I18nStore from '../../../stores/I18nStore';
import Logger from '../../../utils/Logger';
import TypeaheadResultList from '../../typeahead/TypeaheadResultList';
import WizardStore from '../../../stores/WizardStore';

const QUESTION_DTO = "http://onto.fel.cvut.cz/ontologies/s-pipes/question-dto";
const MODULE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-uri";
const MODULE_TYPE_URI = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-module-type-uri";
const SCRIPT_PATH = "http://onto.fel.cvut.cz/ontologies/s-pipes/has-script-path";

export default class WizardBuilder {

    static generateWizard(script, module, moduleType, record, renderCallback) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_TYPE_URI] = moduleType;
        request[MODULE_URI] = module;
        request[SCRIPT_PATH] = script;
        Ajax.post("rest/scripts/forms", request).end((data) => {
            Configuration.actions = Actions;
            Configuration.wizardStore = WizardStore;
            Configuration.intl = I18nStore.getIntl();
            Configuration.typeaheadResultList = TypeaheadResultList;
            WizardGenerator.createWizard(data, record.question, null, renderCallback);
        }, () => {
            Logger.error('Received no valid wizard.');
        });
    }

    static generateFunctionWizard(script, functionUri, record, renderCallback) {
        const request = {};
        request["@type"] = QUESTION_DTO;
        request[MODULE_URI] = functionUri;
        request[SCRIPT_PATH] = script;
        Ajax.post("rest/scripts/functions/forms", request).end((data) => {
            Configuration.actions = Actions;
            Configuration.wizardStore = WizardStore;
            Configuration.intl = I18nStore.getIntl();
            Configuration.typeaheadResultList = TypeaheadResultList;
            WizardGenerator.createWizard(data, record.question, null, renderCallback);
        }, () => {
            Logger.error('Received no valid wizard.');
        });
    }
};