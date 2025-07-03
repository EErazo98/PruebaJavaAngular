// productos.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './productos.component.html',
  styleUrls: ['./productos.component.scss']
})
export class ProductosComponent {
  productos: any[] = [];
  nuevoProducto = {
    nombre: '',
    descripcion: '',
    precio: 0,
    stock: 0
  };

  productoEditando: any = null;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {
    this.obtenerProductos();
  }

  obtenerProductos(): void {
    this.http.get<any[]>('http://localhost:8080/api/productos')
      .subscribe({
        next: (data) => this.productos = data,
        error: (err) => console.error('Error al obtener productos', err)
      });
  }

  crearProducto(): void {
    console.log("SI ENTRO");
    this.http.post('http://localhost:8080/api/productos', this.nuevoProducto)
      .subscribe({
        next: () => {
          this.obtenerProductos();
          this.nuevoProducto = { nombre: '', descripcion: '', precio: 0, stock: 0 };
        },
        error: (err) => console.error('Error al crear producto', err)
      });
  }

  eliminarProducto(id: number): void {
    this.http.delete(`http://localhost:8080/api/productos/${id}`)
      .subscribe({
        next: () => this.obtenerProductos(),
        error: (err) => console.error('Error al eliminar producto', err)
      });
  }

  iniciarEdicion(producto: any): void {
    this.productoEditando = { ...producto };
  }

  cancelarEdicion(): void {
    this.productoEditando = null;
  }

  guardarCambios(): void {
    const { id } = this.productoEditando;
    this.http.put(`http://localhost:8080/api/productos/${id}`, this.productoEditando)
      .subscribe({
        next: () => {
          this.obtenerProductos();
          this.productoEditando = null;
        },
        error: (err) => console.error('Error al actualizar producto', err)
      });
  }

  logout(): void {
    this.authService.logout();
  }
}
