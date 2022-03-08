import MailUserSettings from './components/MailUserSettings.vue';

const components = {
  'mail-user-settings': MailUserSettings,
};

for (const key in components) {
  Vue.component(key, components[key]);
}