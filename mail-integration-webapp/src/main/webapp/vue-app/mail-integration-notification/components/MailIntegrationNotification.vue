<template>
  <div>
    <div @click="clickMailNotification()">
      <user-notification-template
        :notification="notification"
        :message="message"
        :loading="loading"
        url="#">
        <template #avatar>
          <div>
            <v-icon size="36">fa-envelope</v-icon>
          </div>
        </template>
        <template #actions>
          <div class="mt-1">
            <v-btn
              class="btn primary px-2 elevation-0"
              outlined
              small
              @click="clickMailNotification()">
              {{$t('mailIntegration.notification.button.view')}}
            </v-btn>
          </div>
        </template>
      </user-notification-template>
    </div>
    <div id="mailIntegrationNotifDrawer">
    </div>
  </div>
</template>
<script>
export default {
  props: {
    notification: {
      type: Object,
      default: null,
    },
  },
  computed: {
    countNewMessage() {
      return this.notification?.parameters?.NEW_MESSAGES?.split(';')[1].split(',').length;
    },
    message() {
      return this.$t('mailIntegration.notification.drawer.message.received', {
        0: this.countNewMessage
      });
    },
  },
  methods: {
    clickMailNotification() {
      if (!this.$root.secondLevelMailIntegrationVueInstance) {
        const VueMailNotificationDrawer = Vue.extend({
          template: `
            <mail-integration-notification-item />
          `,
        });
        this.$root.secondLevelMailIntegrationVueInstance = new VueMailNotificationDrawer({
          i18n: new VueI18n({
            locale: this.$i18n.locale,
            messages: this.$i18n.messages,
          }),
          vuetify: Vue.prototype.vuetifyOptions,
        }).$mount('#mailIntegrationNotifDrawer');
      }
      document.dispatchEvent(new CustomEvent('open-mail-integration-details-drawer',{ detail: this.notification?.parameters?.NEW_MESSAGES }));

    }
  }
};
</script>
