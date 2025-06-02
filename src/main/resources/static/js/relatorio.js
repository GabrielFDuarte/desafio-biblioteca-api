    const apiUrl = 'http://localhost:8080/api/relatorio';

    async function gerarRelatorio() {
        try {
            const res = await fetch(apiUrl);
            if (!res.ok) {
                throw new Error('Erro ao gerar relatório.');
            }
            const blob = await res.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'relatorio.pdf';
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            const lista = document.getElementById('relatorio');
            lista.innerHTML = `<li class="list-group-item text-danger">${error.message}</li>`;
        }
    }