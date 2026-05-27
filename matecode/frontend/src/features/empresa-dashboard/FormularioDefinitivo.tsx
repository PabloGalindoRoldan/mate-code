interface Props {
    isRectifying: boolean;
}


export default function FormularioDefinitivo({ isRectifying }: Props) {

    return (
        <>
            {isRectifying && <h1>Rectificando</h1>}
            <h1>definitivo</h1>
        </>
    )

}