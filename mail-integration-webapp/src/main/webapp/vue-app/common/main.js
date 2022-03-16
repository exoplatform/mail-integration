import MailIntegrationNotificationAlert from './MailIntegrationNotificationAlert.vue';

const components = {
  'mail-integration-notification-alert': MailIntegrationNotificationAlert,
};

for (const key in components) {
  Vue.component(key, components[key]);
}