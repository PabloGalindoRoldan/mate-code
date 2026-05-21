// PublicacionesTarjeta.tsx
import { useState } from 'react';
import './PublicacionesTarjeta.css';

interface PublicacionTarjetaProps {
    imagen: string;
    alt: string;
    titulo: string;
    contenido: string;
    className?: string;
    fechaCreacion: Date;
}

export default function PublicacionesTarjeta({ imagen, alt, titulo, contenido, className, fechaCreacion }: PublicacionTarjetaProps) {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    return (
        <>
            <article className={`publicacionTarjeta ${className ?? ''}`} onClick={openModal}>
                <div className="publicacionTarjeta__imagenWrapper">
                    <img src={imagen} alt={alt} loading="lazy" />
                </div>
                <div className="publicacionTarjeta__contenido">
                    <span className="publicacionTarjeta__fecha">{new Date(fechaCreacion).toLocaleDateString()}</span>
                    <h3>{titulo}</h3>
                    <p>{contenido}</p>
                    <div className="publicacionTarjeta__footer">
                        <span className="publicacionTarjeta__link">Leer más</span>
                    </div>
                </div>
            </article>

            {/* Ventana Modal Emergente */}
            {isModalOpen && (
                <div className="pub-modal-overlay" onClick={closeModal}>
                    {/* El stopPropagation evita que el modal se cierre al hacer clic adentro del contenido */}
                    <div className="pub-modal-content" onClick={(e) => e.stopPropagation()}>

                        <div className="pub-modal-hero-img">
                            <img src={imagen} alt={alt} />
                        </div>

                        <div className="pub-modal-body">
                            <span className="pub-modal-fecha">{new Date(fechaCreacion).toLocaleDateString()}</span>
                            <h2>{titulo}</h2>
                            <div className="pub-modal-scroll-text">
                                {contenido.split('\n').map((paragraph, index) => (
                                    <p key={index}>{paragraph}</p>
                                ))}
                            </div>
                        </div>

                    </div>
                </div>
            )}
        </>
    );
}