<!--
Copyright (C) 2022 eXo Platform SAS.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
-->
<template>
  <v-app>
    <exo-drawer
      ref="mailIntegrationNotifDrawer"
      id="mailIntegrationNotifDrawer"
      :right="!$vuetify.rtl"
      @closed="closeDrawer"
      disable-pull-to-refresh>
      <template slot="title">
        <div class="flex flex-row">
          <div class="flex flex-column">
            <v-btn
                v-if="isOpened"
                class="flex flex-column me-1"
                icon
                text>
              <v-icon
                  @click="closeDrawer">
                mdi-keyboard-backspace
              </v-icon>
            </v-btn>
          </div>
          <div class="flex flex-column">{{ $t('news.list.settings.title') }}</div>
        </div>
      </template>
      <template slot="content">
        <div
          class="mailIntegrationNotificationItems"
          v-for="(message, index) in messages"
          :key="index">
          <mail-integration-notification-content-item :message="message" />
        </div>
      </template>
    </exo-drawer>
  </v-app>
</template>

<script>
export default {
  data: () => ({
    messages: [],
    dateTimeFormat: {
      hour: '2-digit',
      minute: '2-digit',
    },
    isOpened: false,
  }),
  created() {
    document.addEventListener('open-notification-details-drawer', event => {
      this.retrieveMessages(event.detail);
      this.openDrawer();
    });
  },
  computed: {
    isMobile() {
      return this.$vuetify.breakpoint.name === 'xs' || this.$vuetify.breakpoint.name === 'sm';
    },
    drawerWidth() {
      return '420';
    },
  },
  methods: {
    openDrawer() {
      this.isOpened = true;
      this.$refs.mailIntegrationNotifDrawer.startLoading();
      this.$refs.mailIntegrationNotifDrawer.open();
    },
    closeDrawer() {
      this.$refs.mailIntegrationNotifDrawer.close();
      this.messages = [];
    },
    retrieveMessages(messagesDetail) {
      const mailntegrationSettingId = messagesDetail.split(';')[0];
      const messagesIds = messagesDetail.split(';')[1];
      for (let i = 0; i < messagesIds.split(',').length; i++){
        this.$mailIntegrationService.getMessageById(mailntegrationSettingId, messagesIds.split(',')[i])
          .then(message => {
            this.messages.push(message);
            this.$nextTick(this.$refs.mailIntegrationNotifDrawer.endLoading);
          });
      }
    }
  }
};
</script> 
