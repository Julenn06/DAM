import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { LibroService } from '../../services/libro.service';
import { Libro } from '../../models/libro.model';
import { HeaderComponent } from "../../shared/header/header";

@Component({
  selector: 'app-libros',
  templateUrl: './libros.html',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule, HeaderComponent]
})
export class Libros implements OnInit {
  libros: Libro[] = [];
  librosFiltradas: Libro[] = [];
  
  textoBusquedatitulo = '';
  
  libroEnModal: Partial<Libro> | null = null;
  libroNuevaEnModal: Partial<Libro> | null = null;

  edadMinima: number | null = null;

  constructor(private libroService: LibroService) {}

  ngOnInit() {
    this.loadFilters();
    this.loadlibros();
  }

  loadlibros() {
    this.libroService.getLibros().subscribe((data) => {
      this.libros = data;
      this.filtrarlibros();
    });
  }

  eliminarlibro(id: number | string) {
    this.libroService.deleteLibro(id).subscribe(() => {
      this.loadlibros();
    });
  }

  abrirModalEditar(libro: Libro, dialog?: HTMLDialogElement) {
    this.libroEnModal = { ...libro };
    if (dialog) {
      try {
        dialog.showModal();
      } catch (e) {
        (dialog as any).open = true;
      }
    }
  }

  abrirModalCrear(dialog?: HTMLDialogElement) {
    this.libroNuevaEnModal = {};
    if (dialog) {
      try {
        dialog.showModal();
      } catch (e) {
        (dialog as any).open = true;
      }
    }
  }

  guardarDesdeModalCrear(dialog?: HTMLDialogElement) {
    if (!this.libroNuevaEnModal) return;
    this.libroService.createLibro(this.libroNuevaEnModal).subscribe(() => {
      if (dialog) {
        try {
          dialog.close();
        } catch (e) {
          (dialog as any).open = false;
        }
      }
      this.libroNuevaEnModal = null;
      this.loadlibros();
    });
  }

  cerrarModalCrear(dialog?: HTMLDialogElement) {
    if (dialog) {
      try {
        dialog.close();
      } catch (e) {
        (dialog as any).open = false;
      }
    }
    this.libroNuevaEnModal = null;
  }

  guardarDesdeModal(dialog?: HTMLDialogElement) {
    if (!this.libroEnModal || this.libroEnModal.id == null) return;
    const updated = this.libroEnModal as Libro;
    this.libroService.updateLibro(updated).subscribe(() => {
      if (dialog) {
        try {
          dialog.close();
        } catch (e) {
          (dialog as any).open = false;
        }
      }
      this.libroEnModal = null;
      this.loadlibros();
    });
  }

  cerrarModal(dialog?: HTMLDialogElement) {
    if (dialog) {
      try {
        dialog.close();
      } catch (e) {
        (dialog as any).open = false;
      }
    }
    this.libroEnModal = null;
  }
  
  minmax() {

    let templibros = this.libros;

        templibros = templibros.filter((libro) => Number(libro.anio).toExponential);

    this.librosFiltradas = templibros;
    this.saveFilters();

  }

  filtrarlibros() {
    let templibros = this.libros;

    const busquedatitulo = this.textoBusquedatitulo.trim().toLowerCase();
    if (busquedatitulo) {
      templibros = templibros.filter((libro) =>
        String(libro.autor ?? '').toLowerCase().includes(busquedatitulo)
      );
    }

    this.librosFiltradas = templibros;
    this.saveFilters();
  }

  saveFilters() {
    sessionStorage.setItem('textoBusquedatitulo', this.textoBusquedatitulo);
  }

  loadFilters() {
    const textoBusquedatitulo = sessionStorage.getItem('textoBusquedatitulo');
    if (textoBusquedatitulo !== null) {
      this.textoBusquedatitulo = textoBusquedatitulo;
    }
  }

  limpiarFiltros() {
    this.textoBusquedatitulo = '';
    this.filtrarlibros();
  }
}