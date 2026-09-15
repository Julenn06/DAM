export function kalkulatuLaburpena(produktuak) {
    let batura = 0;
    const kopurua = produktuak.length;
    produktuak.forEach(({ prezioa }) => {
        batura += prezioa;
    });
    return [kopurua, batura];
}
