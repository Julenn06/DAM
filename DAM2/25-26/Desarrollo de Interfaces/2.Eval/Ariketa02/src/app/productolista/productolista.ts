import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Producto } from '../interfaceproducto';
import { Servicioproducto } from '../servicioproducto';

@Component({
  selector: 'app-productolista',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './productolista.html',
  styleUrls: ['./productolista.css']
})
export class Productolista {
  productos: Producto[] = [];
  nuevoProducto: Producto = { 
    nombre: '', 
    descripcion: '', 
    precio: 0, 
    categoria: '', 
    stock: 0, 
    imagen: '', 
    disponible: true,
    descuento: 0
  };
  productoSeleccionado: Producto | null = null;
  productoService: Servicioproducto = inject(Servicioproducto);
  categorias: string[] = ['Electrónica', 'Alimentos', 'Ropa', 'Hogar', 'Deportes', 'Juguetes', 'Libros'];

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.productoService.getProductos().subscribe({
      next: (data) => {
        this.productos = data;
        console.log(this.productos);
      },
      error: (err) => {
        console.error('Error al cargar productos', err);
      },
      complete: () => {}
    });
  }

  seleccionarProducto(producto: Producto) {
    this.productoSeleccionado = producto;
  }

  borrarProducto(id?: string) {
    this.productoService.borrarProducto(id).subscribe({
      next: () => {
        this.productos = this.productos.filter(p => p.id !== id);
      },
      error: (err) => {
        console.error('Error al borrar producto', err);
      }
    });
  }

  actualizarProducto() {
    if (this.productoSeleccionado) {
      this.productoService.actualizarProducto(this.productoSeleccionado).subscribe({
        next: (productoActualizado) => {
          const index = this.productos.findIndex(p => p.id === productoActualizado.id);
          if (index !== -1) {
            this.productos[index] = productoActualizado;
          }
          this.productoSeleccionado = null; // Cerrar el formulario de edición
        },
        error: (err) => {
          console.error('Error al actualizar producto', err);
        }
      });
    }
  }

  crearProducto() {
    this.productoService.agregarProducto(this.nuevoProducto).subscribe({
      next: (producto) => {
        this.productos.push(producto);
        this.nuevoProducto = { 
          nombre: '', 
          descripcion: '', 
          precio: 0, 
          categoria: '', 
          stock: 0, 
          imagen: '', 
          disponible: true,
          descuento: 0
        };
      },
      error: (err) => {
        console.error('Error al crear producto', err);
      }
    });
  }
}
