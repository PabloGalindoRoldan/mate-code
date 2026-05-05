import './PublicacionesTarjeta.css';

interface PublicacionTarjetaProps {
    imagen: string;
    alt: string;
    titulo: string;
    contenido: string;
    className?: string;
}

export default function PublicacionesTarjeta({ imagen, alt, titulo, contenido, className }: PublicacionTarjetaProps) {
    return (
        <article className={`publicacionTarjeta ${className ?? ''}`}>
            <div className="publicacionTarjeta__imagenWrapper">
                <img src={imagen} alt={alt} />
            </div>
            <div className="publicacionTarjeta__contenido">
                <h2>{titulo}</h2>
                <p>{contenido}</p>
            </div>
        </article>
    )
}