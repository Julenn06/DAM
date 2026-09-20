import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { Person } from '../../models/person.model';
import { PersonService } from '../../services/person.service';

@Component({
  selector: 'app-person-table',
  templateUrl: './person.html',
  standalone: true,
  imports: [CommonModule, FormsModule, TranslateModule]
})
export class PersonComponent implements OnInit {
  // Datos de personas desde el servidor
  personas: Person[] = [];
  personasFiltradas: Person[] = [];
  
  // Filtros de búsqueda
  edadMinima: number | null = null;
  textoBusquedaNombre = '';
  textoBusquedaEmail = '';
  
  // Objetos para los modales de creación y edición
  personaEnModal: Partial<Person> | null = null;
  personaNuevaEnModal: Partial<Person> | null = null;

  constructor(private personService: PersonService) {}

  ngOnInit() {
    // Cargar filtros guardados y obtener datos iniciales
    this.loadFilters();
    this.loadPersonas();
  }

  // Obtener listado de personas del servidor
  loadPersonas() {
    this.personService.getPersons().subscribe((data) => {
      this.personas = data;
      this.filtrarPersonas();
    });
  }

  // Eliminar una persona por ID
  eliminarPersona(id: number | string) {
    this.personService.deletePerson(id).subscribe(() => {
      this.loadPersonas();
    });
  }

  // Abrir modal para editar una persona existente
  abrirModalEditar(persona: Person, dialog?: HTMLDialogElement) {
    this.personaEnModal = { ...persona };
    if (dialog) {
      try {
        dialog.showModal();
      } catch (e) {
        (dialog as any).open = true;
      }
    }
  }

  // Abrir modal para crear una nueva persona
  abrirModalCrear(dialog?: HTMLDialogElement) {
    this.personaNuevaEnModal = {};
    if (dialog) {
      try {
        dialog.showModal();
      } catch (e) {
        (dialog as any).open = true;
      }
    }
  }

  /** Guardar la nueva persona creada desde el modal */
  guardarDesdeModalCrear(dialog?: HTMLDialogElement) {
    if (!this.personaNuevaEnModal) return;
    this.personService.createPerson(this.personaNuevaEnModal).subscribe(() => {
      if (dialog) {
        try {
          dialog.close();
        } catch (e) {
          (dialog as any).open = false;
        }
      }
      this.personaNuevaEnModal = null;
      this.loadPersonas();
    });
  }

  /** Cerrar modal de creación sin guardar */
  cerrarModalCrear(dialog?: HTMLDialogElement) {
    if (dialog) {
      try {
        dialog.close();
      } catch (e) {
        (dialog as any).open = false;
      }
    }
    this.personaNuevaEnModal = null;
  }

  // Guardar cambios de una persona en edición
  guardarDesdeModal(dialog?: HTMLDialogElement) {
    if (!this.personaEnModal || this.personaEnModal.id == null) return;
    const updated = this.personaEnModal as Person;
    this.personService.updatePerson(updated).subscribe(() => {
      if (dialog) {
        try {
          dialog.close();
        } catch (e) {
          (dialog as any).open = false;
        }
      }
      this.personaEnModal = null;
      this.loadPersonas();
    });
  }

  // Cerrar modal y descartar cambios
  cerrarModal(dialog?: HTMLDialogElement) {
    if (dialog) {
      try {
        dialog.close();
      } catch (e) {
        (dialog as any).open = false;
      }
    }
    this.personaEnModal = null;
  }

  // Aplicar filtros a la lista de personas
  filtrarPersonas() {
    let tempPersonas = this.personas;

    if (this.edadMinima !== null && this.edadMinima !== undefined) {
      const edadMinima = Number(this.edadMinima);
      if (!Number.isNaN(edadMinima)) {
        tempPersonas = tempPersonas.filter((persona) => Number(persona.edad) > edadMinima);
      }
    }

    const busquedaNombre = this.textoBusquedaNombre.trim().toLowerCase();
    if (busquedaNombre) {
      tempPersonas = tempPersonas.filter((persona) =>
        String(persona.nombre ?? '').toLowerCase().includes(busquedaNombre)
      );
    }

    const busquedaEmail = this.textoBusquedaEmail.trim().toLowerCase();
    if (busquedaEmail) {
      tempPersonas = tempPersonas.filter((persona) =>
        String(persona.email ?? '').toLowerCase().includes(busquedaEmail)
      );
    }

    this.personasFiltradas = tempPersonas;
    this.saveFilters();
  }

  // Guardar filtros en sesión
  saveFilters() {
    sessionStorage.setItem('filtroEdadMinima', this.edadMinima?.toString() || '');
    sessionStorage.setItem('textoBusquedaNombre', this.textoBusquedaNombre);
    sessionStorage.setItem('textoBusquedaEmail', this.textoBusquedaEmail);
  }

  // Recuperar filtros guardados de sesión
  loadFilters() {
    const filtroEdadMinima = sessionStorage.getItem('filtroEdadMinima');
    if (filtroEdadMinima !== null && filtroEdadMinima !== '') {
      this.edadMinima = parseInt(filtroEdadMinima, 10);
    }

    const textoBusquedaNombre = sessionStorage.getItem('textoBusquedaNombre');
    if (textoBusquedaNombre !== null) {
      this.textoBusquedaNombre = textoBusquedaNombre;
    }

    const textoBusquedaEmail = sessionStorage.getItem('textoBusquedaEmail');
    if (textoBusquedaEmail !== null) {
      this.textoBusquedaEmail = textoBusquedaEmail;
    }
  }

  // Limpiar todos los filtros
  limpiarFiltros() {
    this.edadMinima = null;
    this.textoBusquedaNombre = '';
    this.textoBusquedaEmail = '';
    this.filtrarPersonas();
  }
}