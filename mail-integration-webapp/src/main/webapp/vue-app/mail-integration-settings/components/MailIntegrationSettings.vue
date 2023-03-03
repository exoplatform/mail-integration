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
  <v-app v-if="mailIntegrationEnabled && displayed">
    <v-card
      class="border-radius my-4"
      flat>
      <v-list two-line>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="title text-color">
              {{ $t('mailIntegration.settings.connectMail.label') }}
            </v-list-item-title>
            <v-list-item-subtitle class="my-3 text-sub-title font-italic">
              {{ $t('mailIntegration.settings.connectMail.description') }}
            </v-list-item-subtitle>
            <v-list-item-subtitle :title="displayAccountTooltip">
              <span class="my-auto text-capitalize text-truncate"> {{ emailName }} </span>
              <span class="my-auto text-truncate"> {{ account }} </span>
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn
              icon
              @click="openDrawer">
              <i class="uiIconEdit uiIconLightBlue pb-2"></i>
            </v-btn>
            <v-btn
              v-if="editMode"
              icon
              class="mt-6"
              @click="deleteConfirmDialog">
              <i class="uiIconTrash uiIconLightBlue"></i>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card>
    <mail-integration-settings-drawer
      ref="mailIntegrationSettingDrawer"
      :edit-mode="editMode"
      :mail-integration-setting="mailIntegrationSetting"
      @display-alert="displayAlert"
      @mail-integration-settings-save-success="getMailIntegrationSettings" />
    <exo-confirm-dialog
      ref="deleteConfirmDialog"
      :message="$t('mailIntegration.settings.message.confirmDeleteMail')"
      :title="$t('mailIntegration.settings.title.confirmDeleteMail')"
      :ok-label="$t('mailIntegration.settings.ok')"
      :cancel-label="$t('mailIntegration.settings.cancel')"
      @ok="deleteMailIntegrationSetting" />
    <mail-integration-alert />
  </v-app>
</template>

<script>

export default {
  data: () => ({
    mailIntegrationEnabled: false,
    displayed: true,
    editMode: false,
    emailName: '',
    account: '',
    mailIntegrationSetting: [],
  }),
  computed: {
    displayAccountTooltip() {
      return `${this.emailName} ${this.account}`;
    }
  },
  created() {
    this.$featureService.isFeatureEnabled('mailIntegration')
      .then(enabled => this.mailIntegrationEnabled = enabled);
    this.getMailIntegrationSettings();
  },
  mounted() {
    document.addEventListener('hideSettingsApps', (event) => {
      if (event && event.detail && this.id !== event.detail) {
        this.displayed = false;
      }
    });
    document.addEventListener('showSettingsApps', () => this.displayed = true);
  },
  methods: {
    openDrawer(){
      this.$refs.mailIntegrationSettingDrawer.openDrawer();
    },
    getMailIntegrationSettings() {
      this.$mailIntegrationService.getMailIntegrationSettings().then(setting => {
        if (setting && setting.length > 0) {
          this.mailIntegrationSetting = setting[0];
          this.emailName = setting[0].emailName;
          this.account = `(${  setting[0].account  })`;
          this.editMode = true;
        } else {
          this.mailIntegrationSetting = setting;
          this.emailName = '';
          this.account = '';
          this.editMode = false;
        }
      });
    },
    displayAlert(message, type) {
      this.$root.$emit('mail-integration-connection', {
        message,
        type: type || 'success',
      });
    },
    deleteMailIntegrationSetting() {
      this.$mailIntegrationService.deleteMailIntegrationSetting(this.mailIntegrationSetting.id)
        .then(() => {
          this.getMailIntegrationSettings();
        });
    },
    deleteConfirmDialog() {
      this.$refs.deleteConfirmDialog.open();
    },
  }
};
</script>