/**
 * English localization.
 */

import Constants from '../constants/Constants';

module.exports = {
    'locale': 'en',

    'messages': {
        'add': 'Add',
        'back': 'Go Back',
        'cancel': 'Cancel',
        'open': 'Open',
        'close': 'Close',
        'cancel-tooltip': 'Discard changes',
        'save': 'Save',
        'delete': 'Delete',
        'headline': 'Headline',
        'name': 'Name',
        'summary': 'Summary',
        'narrative': 'Narrative',
        'table-actions': 'Actions',
        'table-edit': 'Edit',
        'author': 'Author',
        'description': 'Description',
        'select.default': '--- Select ---',
        'yes': 'Yes',
        'no': 'No',
        'unknown': 'Unknown',
        'please-wait': 'Please wait...',
        'actions': 'Actions',

        'login.title': Constants.APP_NAME + ' - Login',
        'login.username': 'Username',
        'login.password': 'Password',
        'login.submit': 'Login',
        'login.register': 'Register',
        'login.error': 'Authentication failed.',
        'login.progress-mask': 'Logging in...',

        'main.dashboard-nav': 'Dashboard',
        'main.users-nav': 'Users',
        'main.clinics-nav': 'Clinics',
        'main.records-nav': 'Patient records',
        'main.logout': 'Logout',
        'main.my-profile': 'My profile',

        'dashboard.welcome': 'Welcome to ' + Constants.APP_NAME + '.',
        'dashboard.views-tile': 'Browse scripts',
        'dashboard.functions-tile': 'Browse functions',

        'notfound.title': 'Not found',
        'notfound.msg': '{resource}',

        'users.panel-title': 'Users',
        'users.create-user': 'Create user',
        'users.email': 'Email',
        'users.open-tooltip': 'View and edit details of this user',
        'users.delete-tooltip': 'Delete this user',

        'delete.dialog-title': 'Delete item?',
        'delete.dialog-content': 'Are you sure you want to remove {itemLabel}?',

        'user.panel-title': 'User',
        'user.first-name': 'First name',
        'user.last-name': 'Last name',
        'user.username': 'Username',
        'user.password': 'Password',
        'user.password-confirm': 'Confirm password',
        'user.passwords-not-matching-tooltip': 'Passwords don\'t match',
        'user.is-admin': 'Is administrator?',
        'user.save-success': 'User saved successfully',
        'user.save-error': 'Unable to save user. Server responded with {error}.',

        'view.loading-view': 'Loading view...',
        'view.laying-out-view': 'Applying layout...',
        'view.duplicate': 'Duplicate',
        'view.duplicate-new-tab': 'Duplicate current view in a new tab',
        'view.layout.fixed': 'Fixed',
        'view.layout.box': 'Box',
        'view.layout.layered': 'Layered',
        'view.layout.stress': 'Stress',
        'view.layout.mrtree': 'Mr. Tree',
        'view.layout.radial': 'Radial',
        'view.layout.force': 'Force',
        'view.script-changed': 'Script has been changed. Reload view?',
        'view.reload': 'Reload',
        'view.ignore': 'Ignore',
        'view.conflict': 'TTL and form can not be edited at the same time',
        'view.close': 'Close',
        'view.module-type': 'Add module type',
        'view.call-function': 'Call function',

        'records.panel-title': 'Patient records',
        'records.local-name': 'Patient identifier',
        'records.completion-status': 'Completion status',
        'records.completion-status-tooltip.complete': 'All required fields of the patient\'s record have been filled out.',
        'records.completion-status-tooltip.incomplete': 'Some of the required fields of the patient\'s record have not yet been filled out.',
        'records.last-modified': 'Last modified',
        'records.open-tooltip': 'View and edit the record of this patient',
        'records.delete-tooltip': 'Delete this record',

        'record.panel-title': 'Configuration',
        'record.form-title': 'Details',
        'record.clinic': 'Patient treated at',
        'record.created-by-msg': 'Created {date} by {name}.',
        'record.last-edited-msg': 'Last modified {date} by {name}.',
        'record.save-success': 'Patient record successfully saved.',
        'record.save-error': 'Unable to save record. Server responded with {}.',
        'record.form.please-wait': 'Loading form, please wait...',

        'help.local-name': 'Purpose of this entry is to help you reference anonymized patients. Use identifiers such as patient ordering number (e.g. "patient_1", "patient_2"), patient\'s initials (e.g. "M.E."), etc.',

        'wizard.previous': 'Back',
        'wizard.next': 'Next',
        'wizard.finish': 'Finish'
    }
};