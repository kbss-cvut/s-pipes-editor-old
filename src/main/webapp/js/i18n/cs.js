/**
 * Czech localization.
 */

import Constants from '../constants/Constants';

module.exports = {
    'locale': 'cs',

    'messages': {
        'add': 'Přidat',
        'back': 'Zpět',
        'cancel': 'Zrušit',
        'open': 'Otevřít',
        'close': 'Zavřít',
        'cancel-tooltip': 'Zrušit a zahodit změny',
        'save': 'Uložit',
        'delete': 'Smazat',
        'headline': 'Název',
        'name': 'Název',
        'summary': 'Shrnutí',
        'narrative': 'Popis',
        'table-actions': 'Akce',
        'table-edit': 'Editovat',
        'author': 'Autor',
        'description': 'Popis',
        'select.default': '--- Vybrat ---',
        'yes': 'Ano',
        'no': 'Ne',
        'unknown': 'Neznámé',
        'please-wait': 'Prosím, čekejte...',
        'actions': 'Akce',

        'login.title': Constants.APP_NAME + ' - Přihlášení',
        'login.username': 'Uživatelské jméno',
        'login.password': 'Heslo',
        'login.submit': 'Přihlásit',
        'login.register': 'Registrace',
        'login.error': 'Přihlášení se nezdařilo.',
        'login.progress-mask': 'Přihlašuji...',

        'main.dashboard-nav': 'Hlavní strana',
        'main.users-nav': 'Uživatelé',
        'main.clinics-nav': 'Kliniky',
        'main.records-nav': 'Záznamy pacientů',
        'main.logout': 'Odhlásit se',
        'main.my-profile': 'Můj profil',

        'dashboard.welcome': 'Dobrý den, {name}, vítejte v ' + Constants.APP_NAME + '.',
        'dashboard.views-tile': 'Prohlížet skripty',
        'dashboard.functions-tile': 'Prohlížet funkce',

        'notfound.title': 'Nenalezeno',
        'notfound.msg-with-id': 'Záznam \'{resource}\' s identifikátorem {identifier} nenalezen.',
        'notfound.msg': 'Záznam \'{resource}\' nenalezen.',

        'users.panel-title': 'Uživatelé',
        'users.create-user': 'Přidat uživatele',
        'users.email': 'Email',
        'users.open-tooltip': 'Zobrazit či upravit detaily o tomto uživateli',
        'users.delete-tooltip': 'Smazat tohoto uživatele',

        'delete.dialog-title': 'Smazat položku?',
        'delete.dialog-content': 'Určitě chcete odstranit {itemLabel}?',

        'user.panel-title': 'Uživatel',
        'user.first-name': 'Jméno',
        'user.last-name': 'Příjmení',
        'user.username': 'Uživatelské jméno',
        'user.password': 'Heslo',
        'user.password-confirm': 'Potvrzení hesla',
        'user.passwords-not-matching-tooltip': 'Hesla se neshodují',
        'user.is-admin': 'Administrátor?',
        'user.save-success': 'Uživatel úspěšně uložen',
        'user.save-error': 'Uživatele se nepodařilo uložit. Odpověď serveru: {error}',

        'view.loading-view': 'Načítám zobrazení...',
        'view.laying-out-view': 'Uspořádam zobrazení...',
        'view.duplicate': 'Duplikovat',
        'view.duplicate-new-tab': 'Duplikovat zobrazení v nové záložce',
        'view.layout.fixed': 'Fixed',
        'view.layout.box': 'Box',
        'view.layout.layered': 'Layered',
        'view.layout.stress': 'Stress',
        'view.layout.mrtree': 'Mr. Tree',
        'view.layout.radial': 'Radial',
        'view.layout.force': 'Force',
        'view.script-changed': 'Skript se změnil. Obnovit zobrazení?',
        'view.reload': 'Obnovit',
        'view.ignore': 'Ignorovat',
        'view.module-type': 'Typ modulů',
        'view.call-function': 'Zavolat funkce',

        'records.panel-title': 'Záznamy o pacientech',
        'records.local-name': 'Identifikátor pacienta',
        'records.completion-status': 'Stav vyplnění',
        'records.completion-status-tooltip.complete': 'Všechny povinné informace o pacientovi byly vyplněny.',
        'records.completion-status-tooltip.incomplete': 'Některé povinné informace o pacientovi ještě nebyly vyplněny.',
        'records.last-modified': 'Naposledy upraveno',
        'records.open-tooltip': 'Zobrazit či upravit záznam tohoto pacienta',
        'records.delete-tooltip': 'Smazat tento záznam',

        'record.panel-title': 'Konfigurace',
        'record.form-title': 'Details',
        'record.clinic': 'Pacient léčen na klinice',
        'record.created-by-msg': 'Vytvořil(a) {name} {date}.',
        'record.last-edited-msg': 'Naposledy upravil(a) {name} {date}.',
        'record.save-success': 'Záznam o pacientovi úspěšně uložen.',
        'record.save-error': 'Záznam se nepodařilo uložit. Odpověď serveru: {}.',
        'record.form.please-wait': 'Nahrávám formulář, prosím, čekejte...',

        'help.local-name': 'Účelem tohoto atributu je pomoci vám identifikovat anonymizované pacienty. Můžete použít např. číslování pacientů ("pacient_1", "pacient_2") či iniciály pacientů ("M.E.")',

        'wizard.previous': 'Zpět',
        'wizard.next': 'Další',
        'wizard.finish': 'Dokončit'
    }
};