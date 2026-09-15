export interface Producto {
  id?: string;
  nombre: string;
  descripcion: string;
  precio: number;
  categoria: string;
  stock: number;
  imagen: string;
  disponible: boolean;
  fechaCreacion?: string;
  descuento?: number;
}
