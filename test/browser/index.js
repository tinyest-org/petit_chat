async function main() {
    const host = "localhost:8090"

    const req = await fetch(`http://${host}/ws/token-provider`);
    const { token } = await req.json();
    const ws = new WebSocket(`ws://${host}/ws/${token}`);
    ws.onmessage = (e) => {
        console.log(e.data);
    }
    ws.onopen = () => {
        console.log('opened');
    }
}


main()