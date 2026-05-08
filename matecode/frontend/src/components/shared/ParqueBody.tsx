import "./ParqueBody.css"


export default function ParqueBody(params: { empresaInfo: any, isMenuOpen: boolean }) {
    const { isMenuOpen } = params;
    const { empresaInfo } = params;

    return (
        <>
            <div className={`ParqueBodyContainer ${isMenuOpen ? "shrunk" : "full"}`}>
                <h1>Parque Industrial de Viedma</h1>
                <p>{empresaInfo.descripcion}</p>
            </div>
        </>
    )
}