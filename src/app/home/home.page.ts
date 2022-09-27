import { Component } from '@angular/core';
import { NavController } from '@ionic/angular';
import { Plugins } from '@capacitor/core';
import { AlertController } from '@ionic/angular';

const { IdentitySdkPlugin } = Plugins;

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  result: string = ' ';
  username: string;
  password: string;
  merchantID: string;
  uniqueCustomerNumber: string;

  constructor(public alertController: AlertController) {}

  async getResult(){
    const resultData = await IdentitySdkPlugin.getResult();
    this.result = resultData.image;
  }

  async initialize(){
    const resultData = await IdentitySdkPlugin.idm_sdk_init({username: this.username, password: this.password, merchantID: this.merchantID});
    this.result = resultData.result;
    this.presentAlert();
  }

  async idValidation(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID20();
    this.result = resultData.result;
    this.presentAlert();
  }

  async idValidationAndMatchFace(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID10();
    this.result = resultData.result;
    this.presentAlert();
  }

  async idValidationAndcustomerEnroll(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID50({uniqueCustomerNumber: this.uniqueCustomerNumber});
    this.result = resultData.result;
    this.presentAlert();
  }

  async customerEnrollBiometrics(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID175({uniqueCustomerNumber: this.uniqueCustomerNumber});
    this.result = resultData.result;
    this.presentAlert();
  }

  async customerVerification(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID105({uniqueCustomerNumber: this.uniqueCustomerNumber});
    this.result = resultData.result;
    this.presentAlert();
  }

  async identifyCustomer(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID185();
    this.result = resultData.result;
    this.presentAlert();
  }

  async liveFaceCheck(){
    const resultData = await IdentitySdkPlugin.idm_sdk_serviceID660();
    this.result = resultData.result;
    this.presentAlert();
  }

  async submit_result(){
    const resultData = await IdentitySdkPlugin.submit_result();
    this.result = resultData.result;
    this.presentAlert();
  }

  async presentAlert() {
    const alert = await this.alertController.create({
      cssClass: 'my-custom-class',
      header: 'Result',
      message: this.result,
      buttons: ['OK']
    });

    await alert.present();

    const { role } = await alert.onDidDismiss();
    console.log('onDidDismiss resolved with role', role);
  }

}
