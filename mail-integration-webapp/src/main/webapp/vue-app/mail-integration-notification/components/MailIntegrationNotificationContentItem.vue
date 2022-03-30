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
  <v-list class="pa-0">
    <v-list-item-group
      color="primary"
      multiple>
      <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-title>
            <span class="messageReceivedText black--text">{{ $t('UINotification.messageItems.received.MailIntegrationNotificationPlugin') }}</span>
            <span class="senderMail black--text" :title="message.from">{{ message.from }}</span>
          </v-list-item-title>
          <v-list-item-subtitle>
            <div :title="message.subject" class="messageSubject text-truncate">
              {{ $t('UINotification.messageItems.replied.MailIntegrationNotificationPlugin') }}: {{ message.subject }}
            </div>
          </v-list-item-subtitle>
          <v-list-item-subtitle class="sendDate grey--text">
            {{ dateTimeFormat }}
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-icon v-if="hasAttachment">
          <v-icon class="attachmentIcon">mdi-attachment</v-icon>
        </v-list-item-icon>
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
export default {
  props: {
    message: {
      type: Object,
      default: null,
    }
  },
  computed: {
    dateTimeFormat() {
      let formattedDate = null;
      if (this.message && this.message.sentDate) {
        const date = new Date(this.message.sentDate.time);
        const minutes = date.getMinutes() < 10 ? `0${date.getMinutes()}` : date.getMinutes();
        formattedDate = `${date.getHours()}h${minutes}`;
      }
      return formattedDate;
    },
    hasAttachment() {
      return this.message && this.message.attachFiles;
    }
  }
};
</script>