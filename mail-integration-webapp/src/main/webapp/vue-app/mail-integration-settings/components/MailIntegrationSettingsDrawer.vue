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
  <exo-drawer
    ref="mailIntegrationSettingDrawer"
    class="mailIntegrationSettingDrawer"
    body-classes="hide-scroll decrease-z-index-more"
    right>
    <template slot="title">
      {{ $t('mailIntegration.settings.connectMail.drawer.title') }}
    </template>
    <template slot="content">
      <form ref="form1" class="pa-2 ms-2 mt-4">
        <div class="d-flex flex-column flex-grow-1">
          <div class="d-flex flex-column mb-2">
            <label class="d-flex flex-row font-weight-bold my-2">{{ $t('mailIntegration.settings.connectMail.emailAccountName.label') }}</label>
            <div class="d-flex flex-row">
              <v-text-field
                id="emailAccount"
                ref="emailAccount"
                v-model="emailAccount"
                type="string"
                name="emailAccount"
                :placeholder="$t('mailIntegration.settings.connectMail.emailAccountName.placeholder')"
                maxlength="50"
                class="input-block-level ignore-vuetify-classes pa-0"
                counter
                required
                outlined
                dense />
            </div>
          </div>
          <div class="d-flex flex-column mb-2">
            <label class="d-flex flex-row font-weight-bold my-2">{{ $t('mailIntegration.settings.connectMail.imapUrl.label') }}</label>
            <div class="d-flex flex-row">
              <v-text-field
                v-model="imapUrl"
                type="string"
                name="imapUrl"
                :placeholder="$t('mailIntegration.settings.connectMail.imapUrl.placeholder')"
                class="input-block-level ignore-vuetify-classes pa-0"
                outlined
                required
                dense />
            </div>
          </div>
          <div class="d-flex flex-column mb-2">
            <label class="d-flex flex-row font-weight-bold my-2">{{ $t('mailIntegration.settings.connectMail.port.label') }}</label>
            <div class="d-flex flex-row">
              <v-text-field
                v-model="port"
                type="string"
                name="port"
                maxlength="3"
                :error-messages="portErrorMessage"
                :placeholder="$t('mailIntegration.settings.connectMail.port.placeholder')"
                class="input-block-level ignore-vuetify-classes pa-0"
                outlined
                required
                dense />
            </div>
          </div>
          <div class="d-flex flex-column mb-2">
            <label class="d-flex flex-row font-weight-bold my-2">{{ $t('mailIntegration.settings.connectMail.encryption.label') }}</label>
            <div class="d-flex flex-row">
              <v-text-field
                v-model="encryption"
                type="string"
                name="encryption"
                :placeholder="$t('mailIntegration.settings.connectMail.encryption.placeholder')"
                class="input-block-level ignore-vuetify-classes pa-0"
                outlined
                disabled
                dense />
            </div>
          </div>
          <div class="d-flex flex-column mb-2">
            <label class="d-flex flex-row font-weight-bold my-2">{{ $t('mailIntegration.settings.connectMail.account.label') }}</label>
            <div class="d-flex flex-row">
              <v-text-field
                v-model="account"
                type="string"
                name="account"
                :error-messages="accountErrorMessage"
                :placeholder="$t('mailIntegration.settings.connectMail.account.placeholder')"
                class="input-block-level ignore-vuetify-classes pa-0"
                outlined
                required
                dense />
            </div>
          </div>
          <div class="d-flex flex-column mb-2">
            <label class="d-flex flex-row font-weight-bold my-2">{{ $t('mailIntegration.settings.connectMail.password.label') }}</label>
            <div class="d-flex flex-row">
              <v-text-field
                v-model="password"
                :type="toggleFieldType"
                name="password"
                :append-icon="displayPasswordIcon"
                :placeholder="$t('mailIntegration.settings.connectMail.password.placeholder')"
                maxlength="100"
                class="input-block-level ignore-vuetify-classes pa-0"
                required
                outlined
                dense
                @click:append="showPassWord = !showPassWord" />
            </div>
          </div>
        </div>
      </form>
    </template>
    <template slot="footer">
      <div class="d-flex">
        <v-spacer />
        <v-btn
          class="btn me-2"
          @click="close">
          <template>
            {{ $t('mailIntegration.settings.connectMail.cancel') }}
          </template>
        </v-btn>
        <v-btn :disabled="true" class="btn btn-primary">
          {{ $t('mailIntegration.settings.connectMail.save') }}
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>

<script>
const MAIL_PATTERN = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export default {
  data: () => ({
    emailAccount: '',
    imapUrl: '',
    port: '',
    encryption: 'SSL',
    account: '',
    password: '',
    showPassWord: false,
  }),
  computed: {
    portRule() {
      return this.port.length < 4 && this.port.match(/^\d+$/);
    },
    portErrorMessage() {
      return this.portRule || this.port.length === 0 ? '': this.$t('mailIntegration.settings.name.errorPort');
    },
    accountRule() {
      return this.account && this.account.toLowerCase().match(MAIL_PATTERN);
    },
    accountErrorMessage() {
      return this.accountRule || this.account.length === 0 ? '': this.$t('mailIntegration.settings.name.errorMail');
    },
    displayPasswordIcon() {
      return this.showPassWord ? 'mdi-eye': 'mdi-eye-off';
    },
    toggleFieldType() {
      return this.showPassWord ? 'text': 'password';
    },
  },
  methods: {
    openDrawer(){
      this.$refs.mailIntegrationSettingDrawer.open();
    },
    close(){
      this.$refs.mailIntegrationSettingDrawer.close();
    },
  }
};
</script>