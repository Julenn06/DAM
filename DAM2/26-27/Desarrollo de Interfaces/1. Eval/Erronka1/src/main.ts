import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import localeEu from '@angular/common/locales/eu';
import localeEuExtra from '@angular/common/locales/extra/eu';
import { registerLocaleData } from '@angular/common';

registerLocaleData(localeEu, 'eu-ES', localeEuExtra);

bootstrapApplication(App, appConfig).catch((err) => console.error(err));
