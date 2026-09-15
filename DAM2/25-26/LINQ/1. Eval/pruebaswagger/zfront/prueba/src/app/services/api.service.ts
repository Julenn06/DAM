import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ApiResponse<T = any> {
  operacion?: string;
  resultado?: T;
  error?: string;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'http://localhost:64498/api/MetodosService'; // URL exacta del controlador

  constructor(private http: HttpClient) {}

  // === MÉTODOS DE MENSAJES ===
  getHello(): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/hello`);
  }

  postEcho(nombre: string): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/echo`, JSON.stringify(nombre), {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  // === MÉTODOS MATEMÁTICOS ===
  sumar(a: number, b: number): Observable<ApiResponse> {
    const params = new HttpParams()
      .set('a', a.toString())
      .set('b', b.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/sumar`, { params });
  }

  restar(a: number, b: number): Observable<ApiResponse> {
    const params = new HttpParams()
      .set('a', a.toString())
      .set('b', b.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/restar`, { params });
  }

  multiplicar(a: number, b: number): Observable<ApiResponse> {
    const params = new HttpParams()
      .set('a', a.toString())
      .set('b', b.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/multiplicar`, { params });
  }

  dividir(a: number, b: number): Observable<ApiResponse> {
    const params = new HttpParams()
      .set('a', a.toString())
      .set('b', b.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/dividir`, { params });
  }

  potencia(a: number, b: number): Observable<ApiResponse> {
    const params = new HttpParams()
      .set('a', a.toString())
      .set('b', b.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/potencia`, { params });
  }

  factorial(n: number): Observable<ApiResponse> {
    const params = new HttpParams().set('n', n.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/factorial`, { params });
  }

  esPrimo(n: number): Observable<ApiResponse> {
    const params = new HttpParams().set('n', n.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/esprimo`, { params });
  }

  fibonacci(n: number): Observable<ApiResponse> {
    const params = new HttpParams().set('n', n.toString());
    return this.http.get<ApiResponse>(`${this.baseUrl}/fibonacci`, { params });
  }

  // === MÉTODOS DE CADENAS ===
  invertirCadena(s: string): Observable<ApiResponse> {
    const params = new HttpParams().set('s', s);
    return this.http.get<ApiResponse>(`${this.baseUrl}/invertircadena`, { params });
  }

  esPalindromo(s: string): Observable<ApiResponse> {
    const params = new HttpParams().set('s', s);
    return this.http.get<ApiResponse>(`${this.baseUrl}/palindromo`, { params });
  }

  contarVocales(s: string): Observable<ApiResponse> {
    const params = new HttpParams().set('s', s);
    return this.http.get<ApiResponse>(`${this.baseUrl}/contarVocales`, { params });
  }
}
