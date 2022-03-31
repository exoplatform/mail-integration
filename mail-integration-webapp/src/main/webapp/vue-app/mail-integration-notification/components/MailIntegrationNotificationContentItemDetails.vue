<template>
  <div class="contentItemDetails pa-6">
    <div class="flex flex-row text-truncate mailSubject body-2">
      {{ message.subject }}
    </div>
    <div class="flex d-flex mt-4 flex-row">
      <div class="flex d-flex flex-column flex-grow-0 my-auto">
        <v-avatar
          height="32"
          min-height="32"
          width="32"
          min-width="32"
          max-width="32"
          size="32"
          class="my-auto">
          <v-img :src="ownerAvatarUrl" />
        </v-avatar>
      </div>
      <div class="flex d-flex flex-column ms-2 body-2 my-auto">
        <div>{{ message.from }}</div>
        <span class="grey--text caption">{{ sentDate }}</span>
      </div>
    </div>
    <div class="flex d-flex flex-row mt-6 body-2 mailBody">
      message body details message body details message body details message body details
    </div>
    <div v-if="attachmentCount" class="flex d-flex flex-row mt-4 attachmentContent primary--text">
      <v-icon
        color="primary ms-1"
        size="16"
        class="attachmentContentItemIcon">
        mdi-attachment
      </v-icon>
      <span class="primary--text body-2">Piéces jointes ({{ attachmentCount }})</span>
    </div>
    <div v-if="attachmentCount" class="flex flex-row">
      <ul
        v-for="(attachedFile, i) in attachedFiles"
        :key="i"
        class="no-bullets">
        <li class="attachedFileName primary--text text-decoration-underline body-2">{{ attachedFile }}</li>
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
  data: () => ({
    currentUser: null,
    ownerAvatarUrl: ''
  }),
  mounted() {
    this.$userService.getUser(eXo.env.portal.userName).then(user => {
      this.currentUser = user;
      this.ownerAvatarUrl = user.avatar;
    });
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
        return `${date.getDate()}/${Number(date.getMonth())+1}/${date.getFullYear()}`;
      }
      return '';
    }
  }
};
</script>