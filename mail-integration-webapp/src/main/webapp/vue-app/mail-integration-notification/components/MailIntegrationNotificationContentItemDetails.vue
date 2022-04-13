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
  <div class="contentItemDetails pa-6">
    <div class="flex flex-row text-truncate mailSubject body-2">
      {{ message.subject }}
    </div>
    <div class="flex d-flex mt-4 flex-row">
      <div class="flex d-flex flex-column body-2 my-auto">
        <div>{{ message.from }}</div>
        <span class="grey--text caption">{{ sentDate }}</span>
      </div>
    </div>
    <div class="mt-6 body-2 mailBody" v-sanitized-html="messageContent">
    </div>
    <div v-if="attachmentCount" class="flex d-flex flex-row attachmentContent primary--text">
      <v-icon
        color="primary ms-1"
        size="16"
        class="attachmentContentItemIcon">
        mdi-attachment
      </v-icon>
      <span class="primary--text body-2">{{ $t('mailIntegration.notification.drawer.message.attachment') }} ({{ attachmentCount }})</span>
    </div>
    <div v-if="attachmentCount" class="flex flex-row">
      <ul
        v-for="(attachedFile, i) in attachedFiles"
        :key="i"
        class="my-2">
        <mail-integration-notification-attachments :attached-file="attachedFile" />
      </ul>
    </div>
  </div>
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
    attachmentCount() {
      return this.message && this.message.attachedFiles && this.message.attachedFiles.split(',').length;
    },
    attachedFiles() {
      return this.message && this.message.attachedFiles && this.message.attachedFiles.split(',');
    },
    sentDate() {
      if (this.message && this.message.sentDate) {
        const date = new Date(this.message.sentDate.time);
        const month = date.getMonth() + 1 < 10 ? `0${date.getMonth()+1}` : date.getMonth() + 1;
        const day = date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
        return `${day}/${month}/${date.getFullYear()}`;
      }
      return '';
    },
    messageContent() {
      return this.message && this.message.body && this.message.body;
    }
  }
};
</script>