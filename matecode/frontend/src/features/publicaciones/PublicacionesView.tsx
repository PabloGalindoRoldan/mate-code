// PublicacionesView.tsx
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
    fechaCreacion: Date;
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

    // Renderiza 3 tarjetas falsas con animación de carga
    const renderSkeletons = () => (
        <div className="publicacionesContainer">
            {[1, 2, 3].map((n) => (
                <div className="pub-skeleton-card" key={n}>
                    <div className="pub-skeleton-img shimmer"></div>
                    <div className="pub-skeleton-info">
                        <div className="pub-skeleton-title shimmer"></div>
                        <div className="pub-skeleton-text shimmer"></div>
                        <div className="pub-skeleton-text shimmer" style={{ width: '80%' }}></div>
                    </div>
                </div>
            ))}
        </div>
    );

    return (
        <section className="publicacionesSection">
            <div className="publicacionesHeader">
                <h2>Publicaciones y Novedades</h2>
                <div className="publicacionesHeader__line"></div>
            </div>

            {loading ? (
                renderSkeletons()
            ) : publicaciones.length === 0 ? (
                <div className="publicacionesNoData">
                    <p>No hay novedades disponibles en este momento.</p>
                </div>
            ) : (
                <div className="publicacionesContainer">
                    {publicaciones.slice(0, 6).map((pub) => (
                        <PublicacionesTarjeta key={pub.id} {...pub} />
                    ))}
                </div>
            )}
        </section>
    );
}