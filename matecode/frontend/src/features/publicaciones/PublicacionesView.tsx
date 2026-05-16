import React from 'react';
import PublicacionesTarjeta from './PublicacionesTarjeta';
import API from '../../api/axios';
import './PublicacionesView.css';

interface Publicacion {
    id: number;
    titulo: string;
    imagen: string;
    alt: string;
    contenido: string;
}

export default function PublicacionesView() {
    const [publicaciones, setPublicaciones] = React.useState<Publicacion[]>([]);
    const [loading, setLoading] = React.useState(true);

    React.useEffect(() => {
        API.get<Publicacion[]>('/api/publicaciones')
            .then((res) => {
                setPublicaciones(res.data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error cargando comunicados:", err);
                setLoading(false);
            });
    }, []);

    return (
        <div className="publicacionesView">
            <h2>{loading ? "Cargando Publicaciones..." : "Publicaciones"}</h2>
            {!loading && (
                <div className="publicacionesContainer">
                    {publicaciones.length === 0 ? (
                        <p>No hay novedades disponibles en este momento.</p>
                    ) : (
                        publicaciones.map((pub) => (
                            <PublicacionesTarjeta className="PublicacionTarjeta" key={pub.id} {...pub} />
                        ))
                    )}
                </div>
            )}
        </div>
    );
}