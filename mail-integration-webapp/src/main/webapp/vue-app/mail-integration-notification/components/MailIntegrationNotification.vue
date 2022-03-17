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
        <div>
          <span>Your emails</span>
        </div>
      </template>
      <template slot="content">
        <div class="pt-0 pa-5 my-5">
          <div
            v-for="(message, index) in messages"
            :key="index">
            <template>
              <label class="text-subtitle-1 font-weight-bold">
                Mail recu de {{ message.from }}
              </label>
              <label class="ms-2 grey--text">
                {{ message.subject }}
              </label>
              <date-format
                :value="message.sentDate.time"
                :format="dateFormat"
                class="ms-2 grey--text" />
              <date-format
                :value="message.sentDate.time"
                :format="dateTimeFormat"
                class="ms-2 grey--text" />
            </template>
          </div>
        </div>
      </template>
      <template slot="footer">
        <div class="d-flex my-2 flex-row justify-end">
          <v-btn
            class="mx-5 px-8 btn"
            button
            large
            @click="closeDrawer">
            close
          </v-btn>
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
          });
      }
    }
  }
};
</script> 
