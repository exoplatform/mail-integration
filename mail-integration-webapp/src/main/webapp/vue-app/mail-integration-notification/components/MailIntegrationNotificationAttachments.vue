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
  <li class="d-flex flex-row attachedFileName primary--text text-decoration-underline body-2" style="justify-content:inherit;">
    <v-icon
      :color="documentIcon.color"
      class="flex-grow-0 me-2"
      size="20">
      {{ documentIcon.icon }}
    </v-icon>
    <div class="text-truncate">{{ attachedFileName }}</div>
  </li>
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
      } else if (attachedFileIcon === 'csv') {
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
        icon.color = '#476A9C';
      }
      return icon;
    },
  }
};
</script>
