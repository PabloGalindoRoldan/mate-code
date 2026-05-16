import React from 'react';
import PublicacionesTarjeta from './PublicacionesTarjeta';
import './PublicacionesView.css';
import data from '../../../tmp/publicaciones.json';

interface Publicacion {
    id: number;
    titulo: string;
    imagen: string;
    alt: string;
    contenido: string;
}

export default function PublicacionesView() {

    const [publicaciones, setPublicaciones] = React.useState<Publicacion[]>([]);

    React.useEffect(() => {
        setPublicaciones(data.publicaciones);
    }, []);

    return (
        <div className="publicacionesView">
            <h2>Publicaciones</h2>
            <div className="publicacionesContainer">
                {publicaciones.map((pub) => (
                    <PublicacionesTarjeta className="PublicacionTarjeta" key={pub.id} {...pub} />
                ))}
            </div>
        </div>
    )
}