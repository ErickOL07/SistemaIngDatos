package tda;

public class Cola<T> {

    // Atributos

    private Nodo<T> frente;
    private Nodo<T> ultimo;

    // Constructores

    public Cola() {
        frente = null;
        ultimo = null;
    }

    // Si la cola esta vacia

    public boolean esVacia() {
        if (frente == null) {
            return true;
        } else {
            return false;
        }
    }

    // encolar: agregar un nuevo elemento a al final de la cola

    public void encolar(T pElemento) {
        if (esVacia() == true) {
            Nodo<T> nuevoNodo = new Nodo(pElemento);
            frente = nuevoNodo;
            ultimo = nuevoNodo;
        } else { // la cola no esta vacia
            Nodo<T> nuevoNodo = new Nodo(pElemento);
            ultimo.setNext(nuevoNodo);
            ultimo = nuevoNodo;

        }
    }

    // desencolar: eliminar el elemento que esta al frente de la cola
    // nos devuelve el elemento eliminado

    public T desencolar() {
        if (esVacia() == false) {
            T x = frente.getData();
            Nodo<T> aux = frente;
            frente = aux.getNext();
            return x;
        } else {
            throw new RuntimeException("ERROR: no es posible desencolar");
        }
    }

    public T frente() {
        if (!esVacia()) {
            return frente.getData();
        } else {
            throw new RuntimeException("ERROR: no es posible devolver frente");
        }
    }

    public int longitud() {
        int i = 0;
        Nodo<T> aux = frente;
        while (aux != null) {
            aux = aux.getNext();
            i++;
        }
        return i;
    }
    
    @Override
    public String toString() {
        String s = "[";
        Nodo<T> actual = frente;
        while (actual != null) {
            s += "\n -"+ actual.getData().toString();
            if (actual.getNext() != null) {
                s += ", ";
            }
            actual = actual.getNext();
        }
        s += "]";
        return s;
    }

}
