<template>
  <v-app>
    <v-card
      class="border-radius ma-4"
      flat>
      <v-list two-line>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="title text-color">
              {{ $t('exoplatform.mailIntegration.settings.connectMail.label') }}
            </v-list-item-title>
            <v-list-item-subtitle class="my-3 text-sub-title font-italic">
              {{ $t('exoplatform.mailIntegration.settings.connectMail.description') }}
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn icon>
              <i class="uiIconEdit uiIconLightBlue pb-2"></i>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-card>
  </v-app>
</template>