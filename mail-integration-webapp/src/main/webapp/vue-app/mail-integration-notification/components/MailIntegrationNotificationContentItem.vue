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
        formattedDate = `${date.getHours()}h${date.getMinutes() < 10 && `0${date.getMinutes()}` || date.getMinutes()}`;
      }
      return formattedDate;
    },
    hasAttachment() {
      return this.message && this.message.attachFiles;
    }
  }
};
</script>