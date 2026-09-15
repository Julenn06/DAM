import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService, ApiResponse } from './services/api.service';

@Component({
  selector: 'app-root',
  imports: [FormsModule, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('PruebaSwagger Frontend');

  // Navigation items for the pill group
  navigationItems = [
    { title: 'Explore the Docs', link: 'https://angular.dev' },
    { title: 'Learn with Tutorials', link: 'https://angular.dev/tutorials' },
    { title: 'Prompt and best practices for AI', link: 'https://angular.dev/ai/develop-with-ai'},
    { title: 'CLI Docs', link: 'https://angular.dev/tools/cli' },
    { title: 'Angular Language Service', link: 'https://angular.dev/tools/language-service' },
    { title: 'Angular DevTools', link: 'https://angular.dev/tools/devtools' },
  ];

  // Signals para el estado de la aplicación
  loading = signal(false);
  resultado = signal<string | null>(null);
  error = signal<string | null>(null);

  // Formularios para diferentes operaciones
  matematicasForm = {
    a: 0,
    b: 0,
    n: 0
  };

  cadenaForm = {
    texto: ''
  };

  mensajeForm = {
    nombre: ''
  };

  constructor(private apiService: ApiService) {}

  // Función para formatear respuestas de manera elegante
  private formatearRespuesta(tipo: string, respuesta: any, entrada?: any): string {
    switch (tipo.toLowerCase()) {
      case 'mensaje':
        return `💬 ${respuesta}`;

      case 'echo':
        return `📢 ${respuesta}`;

      case 'suma':
        return `➕ ${entrada.a} + ${entrada.b} = ${respuesta.resultado}`;

      case 'resta':
        return `➖ ${entrada.a} - ${entrada.b} = ${respuesta.resultado}`;

      case 'multiplicación':
        return `✖️ ${entrada.a} × ${entrada.b} = ${respuesta.resultado}`;

      case 'división':
        return `➗ ${entrada.a} ÷ ${entrada.b} = ${respuesta.resultado}`;

      case 'potencia':
        return `🔺 ${entrada.a}^${entrada.b} = ${respuesta.resultado}`;

      case 'factorial':
        return `❗ ${entrada.n}! = ${respuesta.resultado}`;

      case 'es primo':
        const esPrimo = respuesta.resultado ? 'SÍ es primo' : 'NO es primo';
        return `🔍 ${entrada.n} → ${esPrimo}`;

      case 'fibonacci':
        return `🌀 Fibonacci(${entrada.n}) = ${respuesta.resultado}`;

      case 'invertir cadena':
        return `🔄 "${entrada.texto}" → "${respuesta.resultado}"`;

      case 'es palíndromo':
        const esPalindromo = respuesta.resultado ? 'SÍ es palíndromo' : 'NO es palíndromo';
        return `🔍 "${entrada.texto}" → ${esPalindromo}`;

      case 'contar vocales':
        const vocal = respuesta.resultado === 1 ? 'vocal' : 'vocales';
        return `🔤 "${entrada.texto}" tiene ${respuesta.resultado} ${vocal}`;

      default:
        return `✅ Resultado: ${respuesta.resultado || respuesta}`;
    }
  }

  // === MÉTODOS DE MENSAJES ===
  async getHello() {
    this.setLoading(true);
    try {
      const result = await this.apiService.getHello().toPromise();
      const mensaje = this.formatearRespuesta('mensaje', result);
      this.setResultado(mensaje);
    } catch (error) {
      this.setError('Error al obtener el mensaje');
    }
    this.setLoading(false);
  }

  async postEcho() {
    if (!this.mensajeForm.nombre.trim()) {
      this.setError('Por favor, ingrese un nombre');
      return;
    }

    this.setLoading(true);
    try {
      const result = await this.apiService.postEcho(this.mensajeForm.nombre).toPromise();
      const mensaje = this.formatearRespuesta('echo', result);
      this.setResultado(mensaje);
    } catch (error) {
      this.setError('Error al enviar el mensaje');
    }
    this.setLoading(false);
  }  // === MÉTODOS MATEMÁTICOS ===
  async sumar() {
    await this.operacionMatematica('sumar', 'Suma');
  }

  async restar() {
    await this.operacionMatematica('restar', 'Resta');
  }

  async multiplicar() {
    await this.operacionMatematica('multiplicar', 'Multiplicación');
  }

  async dividir() {
    await this.operacionMatematica('dividir', 'División');
  }

  async potencia() {
    await this.operacionMatematica('potencia', 'Potencia');
  }

  async factorial() {
    await this.operacionUnaria('factorial', 'Factorial');
  }

  async esPrimo() {
    await this.operacionUnaria('esPrimo', 'Es Primo');
  }

  async fibonacci() {
    await this.operacionUnaria('fibonacci', 'Fibonacci');
  }

  // === MÉTODOS DE CADENAS ===
  async invertirCadena() {
    await this.operacionCadena('invertirCadena', 'Invertir Cadena');
  }

  async esPalindromo() {
    await this.operacionCadena('esPalindromo', 'Es Palíndromo');
  }

  async contarVocales() {
    await this.operacionCadena('contarVocales', 'Contar Vocales');
  }

  // === MÉTODOS AUXILIARES ===
  private async operacionMatematica(operacion: string, tipo: string) {
    this.setLoading(true);
    try {
      let result: ApiResponse | undefined;

      switch (operacion) {
        case 'sumar':
          result = await this.apiService.sumar(this.matematicasForm.a, this.matematicasForm.b).toPromise();
          break;
        case 'restar':
          result = await this.apiService.restar(this.matematicasForm.a, this.matematicasForm.b).toPromise();
          break;
        case 'multiplicar':
          result = await this.apiService.multiplicar(this.matematicasForm.a, this.matematicasForm.b).toPromise();
          break;
        case 'dividir':
          result = await this.apiService.dividir(this.matematicasForm.a, this.matematicasForm.b).toPromise();
          break;
        case 'potencia':
          result = await this.apiService.potencia(this.matematicasForm.a, this.matematicasForm.b).toPromise();
          break;
      }

      if (result?.error) {
        this.setError(result.error);
      } else {
        const entrada = { a: this.matematicasForm.a, b: this.matematicasForm.b };
        const mensaje = this.formatearRespuesta(tipo, result, entrada);
        this.setResultado(mensaje);
      }
    } catch (error) {
      this.setError(`Error en ${tipo.toLowerCase()}`);
    }
    this.setLoading(false);
  }

  private async operacionUnaria(operacion: string, tipo: string) {
    this.setLoading(true);
    try {
      let result: ApiResponse | undefined;

      switch (operacion) {
        case 'factorial':
          result = await this.apiService.factorial(this.matematicasForm.n).toPromise();
          break;
        case 'esPrimo':
          result = await this.apiService.esPrimo(this.matematicasForm.n).toPromise();
          break;
        case 'fibonacci':
          result = await this.apiService.fibonacci(this.matematicasForm.n).toPromise();
          break;
      }

      if (result?.error) {
        this.setError(result.error);
      } else {
        const entrada = { n: this.matematicasForm.n };
        const mensaje = this.formatearRespuesta(tipo, result, entrada);
        this.setResultado(mensaje);
      }
    } catch (error) {
      this.setError(`Error en ${tipo.toLowerCase()}`);
    }
    this.setLoading(false);
  }

  private async operacionCadena(operacion: string, tipo: string) {
    if (!this.cadenaForm.texto.trim()) {
      this.setError('Por favor, ingrese un texto');
      return;
    }

    this.setLoading(true);
    try {
      let result: ApiResponse | undefined;

      switch (operacion) {
        case 'invertirCadena':
          result = await this.apiService.invertirCadena(this.cadenaForm.texto).toPromise();
          break;
        case 'esPalindromo':
          result = await this.apiService.esPalindromo(this.cadenaForm.texto).toPromise();
          break;
        case 'contarVocales':
          result = await this.apiService.contarVocales(this.cadenaForm.texto).toPromise();
          break;
      }

      if (result?.error) {
        this.setError(result.error);
      } else {
        const entrada = { texto: this.cadenaForm.texto };
        const mensaje = this.formatearRespuesta(tipo, result, entrada);
        this.setResultado(mensaje);
      }
    } catch (error) {
      this.setError(`Error en ${tipo.toLowerCase()}`);
    }
    this.setLoading(false);
  }

  private setLoading(loading: boolean) {
    this.loading.set(loading);
    if (loading) {
      this.error.set(null);
    }
  }

  private setResultado(resultado: string) {
    this.resultado.set(resultado);
    this.error.set(null);
  }

  private setError(error: string) {
    this.error.set(error);
    this.resultado.set(null);
  }

  limpiarResultados() {
    this.resultado.set(null);
    this.error.set(null);
  }
}
