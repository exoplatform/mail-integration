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
      allow-expand
      @closed="closeDrawer"
      @expand-updated="expanded = $event"
      disable-pull-to-refresh>
      <template slot="title">
        <div class="flex d-flex flex-row">
          <v-btn
            v-if="isOpened"
            icon
            text>
            <v-icon
              @click="backDrawer">
              fa solid fa-arrow-left
            </v-icon>
          </v-btn>
          <span class="flex flex-column my-auto">{{ $t('mailIntegration.notification.drawer.title') }}</span>
        </div>
      </template>
      <template slot="content">
        <div
          class="mailIntegrationNotificationItems"
          v-for="(message, index) in messages"
          :key="index">
          <mail-integration-notification-content-item
            v-if="step === 1"
            :message="message"
            :step="step"
            @second-step="updateInformation" />
        </div>
        <mail-integration-notification-content-item-details v-if="step === 2" :message="selectedMessage" />
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
    selectedMessage: null,
    isOpened: false,
    step: 0,
    expanded: false,
  }),
  created() {
    document.addEventListener('open-mail-integration-details-drawer', event => {
      this.retrieveMessages(event.detail);
      this.openDrawer();
    });
  },
  watch: {
    expanded(newValue) {
      eXo.openedDrawers.forEach((drawer) => drawer.expand=newValue);
    },
  },
  methods: {
    openDrawer() {
      this.isOpened = true;
      this.step = 1;
      this.$refs.mailIntegrationNotifDrawer.startLoading();
      this.$refs.mailIntegrationNotifDrawer.open();
    },
    closeDrawer() {
      this.$refs.mailIntegrationNotifDrawer.close();
    },
    retrieveMessages(messagesDetail) {
      this.messages = [];
      const mailntegrationSettingId = messagesDetail.split(';')[0];
      const messagesIds = messagesDetail.split(';')[1];
      for (let i = 0; i < messagesIds.split(',').length; i++){
        this.$mailIntegrationService.getMessageById(mailntegrationSettingId, messagesIds.split(',')[i])
          .then(message => {
            this.messages.push(message);
            this.$nextTick(this.$refs.mailIntegrationNotifDrawer.endLoading);
          });
      }
    },
    backDrawer() {
      if (this.step === 2) {
        this.step = 1;
        this.$root.$emit('close-details-item', this.step);
      } else {
        this.closeDrawer();
      }
    },
    updateInformation(step, selectedMessage) {
      this.step = step;
      this.selectedMessage = selectedMessage;
    }
  }
};
</script> 
