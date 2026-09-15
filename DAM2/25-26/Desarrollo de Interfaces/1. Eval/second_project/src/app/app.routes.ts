import { Routes } from '@angular/router';
import { SumaRestaComponent} from './pages/suma_resta/suma_resta.component';
import { WelcomeComponent} from './pages/welcome/welcome.component';
import { LoginComponent } from './pages/login/login.component';
import { Arrays } from './pages/arrays/arrays';

export const routes: Routes = [

    { path: 'login', component: LoginComponent },
    { path: 'welcome', component: WelcomeComponent },
    { path: 'suma_resta', component: SumaRestaComponent },
    { path: 'arrays', component: Arrays },
    { path: '**', redirectTo: 'login' }

];