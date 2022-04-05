<template>
  <div>
    <li class="attachedFileName primary--text text-decoration-underline body-2">
      <v-icon
        :color="documentIcon.color"
        class="me-2"
        size="20">
        {{ documentIcon.icon }}
      </v-icon>
      <span>{{ attachedFileName }}</span>
    </li>
  </div>
</template>

<script>
export default {
  props: {
    attachedFile: {
      type: Object,
      default: null,
    },
  },
  computed: {
    attachedFileName() {
      return this.attachedFile && this.attachedFile.length > 0 && this.attachedFile.split('*')[0];
    },
    documentIcon() {
      const icon = {};
      const attachedFileIcon = this.attachedFile && this.attachedFile.length > 0 && this.attachedFile.split('*')[1];
      if (attachedFileIcon === 'pdf') {
        icon.icon = 'fas fa-file-pdf';
        icon.color = '#FF0000';
      } else if (attachedFileIcon === 'powerpoint') {
        icon.icon = 'fas fa-file-powerpoint';
        icon.color = '#CB4B32';
      } else if (attachedFileIcon === 'excel') {
        icon.icon = 'fas fa-file-excel';
        icon.color = '#217345';
      } else if (attachedFileIcon === 'word') {
        icon.icon = 'fas fa-file-word';
        icon.color = '#2A5699';
      } else if (attachedFileIcon === 'image') {
        icon.icon = 'fas fa-file-image';
        icon.color = '#999999';
      } else if (attachedFileIcon === 'video') {
        icon.icon = 'fas fa-file-video';
        icon.color =  '#79577A';
      } else if (attachedFileIcon === 'archive') {
        icon.icon = 'fas fa-file-archive';
        icon.color = '#717272';
      } else {
        icon.icon = 'fas fa-file';
        icon.color = '#578DC9';
      }
      return icon;
    },
  }
};
</script>