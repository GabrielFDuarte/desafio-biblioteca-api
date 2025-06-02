    const apiUrl = 'http://localhost:8080/api/livros';
    const campoValor = document.getElementById('campoValor');
    const valorMin = document.getElementById('valorMin');
    const valorMax = document.getElementById('valorMax');
    const valorEdit = document.getElementById('valorEdit');

    const modalAutores = new bootstrap.Modal(document.getElementById('modalPromptAutores'));
    const modalAssuntos = new bootstrap.Modal(document.getElementById('modalPromptAssuntos'));
    let idLivroAtual;

    const notyf = new Notyf({
            duration: 3000,
            position: {x: 'right', y: 'top'},
            types: [
                {type: 'error', background: 'indianred', icon: false},
                {type: 'success', background: 'seagreen', icon: false}
            ]
    });

    campoValor.value = 'R$0,00';
    valorMin.value = 'R$0,00';
    valorMax.value = 'R$0,00';
    valorEdit.value = 'R$0,00';

    async function listarLivros() {
        const res = await fetch(apiUrl);
        const livros = await res.json();
        const lista = document.getElementById('listaLivros');
        lista.innerHTML = '';

        livros.forEach(livro => {
            const li = document.createElement('li');
            li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');

            li.innerHTML = `
                <span>${livro.codL} - ${livro.titulo} (${livro.anoPublicacao})</span>
                <div>
                    <button class="btn btn-sm btn-outline-primary me-2" onclick="abrirModalAutores(${livro.codL})">Editar Autores</button>
                    <button class="btn btn-sm btn-outline-primary me-2" onclick="abrirModalAssuntos(${livro.codL})">Editar Assuntos</button>
                    <button class="btn btn-danger btn-sm" onclick="deletarLivro(${livro.codL})">Excluir</button>
                </div>
            `;
            lista.appendChild(li);
        });
    }

    async function abrirModalAutores(idLivro) {
        idLivroAtual = idLivro;
        modalAutores.show();
    }

    async function abrirModalAssuntos(idLivro) {
        idLivroAtual = idLivro;
        modalAssuntos.show();
    }

    document.getElementById('salvarAutores').addEventListener('click', function () {
        const valorAutores = document.getElementById('autor-text').value;
        editarAutoresDoLivro(valorAutores);
    });

    document.getElementById('salvarAssunto').addEventListener('click', function () {
        const valorAssuntos = document.getElementById('assunto-text').value;
        editarAssuntosDoLivro(valorAssuntos);
    });

    async function editarAutoresDoLivro(valorAutores) {
            if (!valorAutores) return;

            const inputLimpo = limparString(valorAutores);

            const ids = inputLimpo.split(',')
                             .map(id => parseInt(id.trim()))
                             .filter(id => !isNaN(id));

            if (ids.length === 0) {
                exibirToastError("Nenhum ID válido informado.");
                return;
            }

            try {
                const res = await fetch(`${apiUrl}/${idLivroAtual}/atualiza-autores`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(ids)
                });

                if (res.ok) {
                    exibirToastSuccess("Autores atualizados com sucesso!");
                } else {
                    const erro = await res.json();
                    const mensagemErro = erro.message || 'Erro ao atualizar autores.';
                    exibirToastError(mensagemErro);
                }
            } catch (err) {
                console.error(err);
                exibirToastError("Erro na requisição.");
            }
            idLivroAtual = null;
            document.getElementById('autor-text').value = '';
            modalAutores.hide()
        }

    async function editarAssuntosDoLivro(valorAssuntos) {
            if (!valorAssuntos) return;

            const inputLimpo = limparString(valorAssuntos);

            const ids = inputLimpo.split(',')
                             .map(id => parseInt(id.trim()))
                             .filter(id => !isNaN(id));

            if (ids.length === 0) {
                exibirToastError("Nenhum ID válido informado.");
                return;
            }

            try {
                const res = await fetch(`${apiUrl}/${idLivroAtual}/atualiza-assuntos`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(ids)
                });

                if (res.ok) {
                    exibirToastSuccess("Assuntos atualizados com sucesso!");
                } else {
                    const erro = await res.json();
                    const mensagemErro = erro.message || 'Erro ao atualizar assuntos.';
                    exibirToastError(mensagemErro);
                }
            } catch (err) {
                console.error(err);
                exibirToastError("Erro na requisição.");
            }
            idLivroAtual = null;
            document.getElementById('assunto-text').value = '';
            modalAssuntos.hide();
        }

    async function deletarLivro(id) {
        if (!confirm('Tem certeza que deseja excluir este livro?')) return;

        const res = await fetch(`${apiUrl}/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            exibirToastSuccess("Livro excluído com sucesso!");
            listarLivros();
        } else {
            const erro = await res.json();
            const mensagemErro = erro.message || 'Erro ao excluir o livro.';
            exibirToastError(mensagemErro);
        }
    }

    document.getElementById('formCadastrarLivro').addEventListener('submit', async (e) => {
        e.preventDefault();

        const form = e.target;

        const inputLimpoAutores = limparString(form.autoresIds.value);
        const inputLimpoAssuntos = limparString(form.assuntosIds.value);

        const data = {
            titulo: form.titulo.value,
            editora: form.editora.value,
            edicao: parseInt(form.edicao.value),
            anoPublicacao: form.anoPublicacao.value,
            valor: parseFloat(form.valor.value.replace(/[R$\s\.]/g, '').replace(',','.')),
            autoresIds: inputLimpoAutores.split(',').map(id => parseInt(id.trim())),
            assuntosIds: inputLimpoAssuntos.split(',').map(id => parseInt(id.trim()))
        };
        const res = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            exibirToastSuccess("Livro cadastrado com sucesso!");
            form.reset();
        } else {
            const erro = await res.json();
            const mensagemErro = erro.message || 'Erro ao cadastrar livro.';
            exibirToastError(mensagemErro);
        }
        listarLivros()
    });

    document.getElementById('formBuscarLivro').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = e.target.id.value;
        const res = await fetch(`${apiUrl}/${id}`);
        const container = document.getElementById('detalhesLivro');
        if (res.ok) {
            const livro = await res.json();
            container.innerHTML = `
                <div class="card mt-3">
                    <div class="card-body">
                        <h4>${livro.titulo}</h4>
                        <p><strong>Editora:</strong> ${livro.editora}</p>
                        <p><strong>Edição:</strong> ${livro.edicao}</p>
                        <p><strong>Ano:</strong> ${livro.anoPublicacao}</p>
                        <p><strong>Valor:</strong> R$ ${livro.valor.toFixed(2)}</p>
                        <p><strong>Autores:</strong> ${livro.autores.map(a => a.nome).join(', ')}</p>
                        <p><strong>Assuntos:</strong> ${livro.assuntos.map(a => a.descricao).join(', ')}</p>
                    </div>
                </div>
            `;
        } else {
            const erro = await res.json();
            const mensagemErro = erro.message || 'Livro não encontrado.';
            container.innerHTML = '<p class="text-danger">'+mensagemErro+'</p>';
        }
    });

    document.getElementById('formBuscarLivroPorTitulo').addEventListener('submit', async (e) => {
        e.preventDefault();
        const titulo = e.target.titulo.value;
        const res = await fetch(`${apiUrl}/buscar?titulo=${encodeURIComponent(titulo)}`);
        const listaResultado = document.getElementById('resultadoBuscaTitulo');
        listaResultado.innerHTML = '';

        if (res.ok) {
            const livros = await res.json();
            if (livros.length === 0) {
                listaResultado.innerHTML = '<li class="list-group-item text-danger">Nenhum livro encontrado.</li>';
            } else {
                livros.forEach(livro => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.textContent = `${livro.codL} - ${livro.titulo} (${livro.anoPublicacao})`;
                    listaResultado.appendChild(li);
                });
            }
        } else {
            const erro = await res.json();
            const original = erro.message || 'Erro ao buscar livros.';
            const mensagemErro = original.replace("título/autor/assunto", "título");
            listaResultado.innerHTML = '<li class="list-group-item text-danger">'+mensagemErro+'</li>';
        }
    });

    document.getElementById('formBuscarLivroPorAutorOuAssunto').addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = e.target.id.value;
        const tipo = e.target.tipo.value;
        const listaResultado = document.getElementById('resultadoBuscaTipo');
        listaResultado.innerHTML = '';

        if (!id || !tipo) {
            listaResultado.innerHTML = '<li class="list-group-item text-danger">Preencha todos os campos.</li>';
            return;
        }

        const res = await fetch(`${apiUrl}/${tipo}/${id}`);
        if (res.ok) {
            const livros = await res.json();
            if (livros.length === 0) {
                listaResultado.innerHTML = '<li class="list-group-item text-warning">Nenhum livro encontrado.</li>';
            } else {
                livros.forEach(livro => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.textContent = `${livro.codL} - ${livro.titulo} (${livro.anoPublicacao})`;
                    listaResultado.appendChild(li);
                });
            }
        } else {
            const erro = await res.json();
            const mensagemErro = erro.message || 'Erro ao buscar livros.';
            listaResultado.innerHTML = '<li class="list-group-item text-danger">'+mensagemErro+'</li>';
        }
    });

    document.getElementById('formBuscarPorPreco').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const valorMin = parseFloat(form.valorMin.value.replace(/[R$\s\.]/g, '').replace(',',''));
        const valorMax = parseFloat(form.valorMax.value.replace(/[R$\s\.]/g, '').replace(',',''));

        const res = await fetch(`${apiUrl}/faixa-preco?valorMin=${valorMin}&valorMax=${valorMax}`);
        const lista = document.getElementById('resultadoBuscaPreco');
        lista.innerHTML = '';

        if (res.ok) {
            const livros = await res.json();
            if (livros.length === 0) {
                lista.innerHTML = '<li class="list-group-item text-danger">Nenhum livro encontrado na faixa de preço.</li>';
            } else {
                livros.forEach(livro => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item');
                    li.innerHTML = `
                        <strong>${livro.titulo}</strong> - R$ ${livro.valor.toFixed(2)}<br/>
                        Editora: ${livro.editora} | Ano: ${livro.anoPublicacao}
                    `;
                    lista.appendChild(li);
                });
            }
        } else {
            const erro = await res.json();
            const erroValor = erro.message || 'Erro ao buscar livros por faixa de preço.';
            mensagemErro = formatarValoresEmMensagem(erroValor);
            lista.innerHTML = '<li class="list-group-item text-danger">'+mensagemErro+'</li>';
        }
    });

    function formatarValoresEmMensagem(mensagem) {
        return mensagem.replace(/\b\d+\b/g, (numero) => {
            const valor = parseInt(numero, 10);
            if (isNaN(valor)) return numero;
            return (valor / 100).toLocaleString('pt-BR', {
                style: 'currency',
                currency: 'BRL'
            });
        });
    }

    document.getElementById('formEditarLivro').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const id = form.id.value;

        const inputLimpoAutores = limparString(form.autoresIds.value);
        const inputLimpoAssuntos = limparString(form.assuntosIds.value);

        const data = {
            titulo: form.titulo.value,
            editora: form.editora.value,
            edicao: parseInt(form.edicao.value),
            anoPublicacao: form.anoPublicacao.value,
            valor: parseFloat(form.valor.value.replace(/[R$\s\.]/g, '').replace(',','.')),
            autoresIds: inputLimpoAutores.split(',').map(id => parseInt(id.trim())),
            assuntosIds: inputLimpoAssuntos.split(',').map(id => parseInt(id.trim()))
        };

        const res = await fetch(`${apiUrl}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            exibirToastSuccess("Livro editado com sucesso!");
            listarLivros();
            form.reset();
        } else {
            const erro = await res.json();
            const mensagemErro = erro.message || 'Erro ao editar livro.';
            exibirToastError(mensagemErro);
        }
    });

    campoValor.addEventListener('input', () => {
        let valor = campoValor.value.replace(/\D/g, '');

        if (!valor) {
            campoValor.value = 'R$0,00';
            return;
        }

        valor = (parseInt(valor, 10) / 100).toFixed(2);
        campoValor.value = `R$${Number(valor).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
    });

    valorMin.addEventListener('input', () => {
        let valor = valorMin.value.replace(/\D/g, '');

        if (!valor) {
            valorMin.value = 'R$0,00';
            return;
        }

        valor = (parseInt(valor, 10) / 100).toFixed(2);
        valorMin.value = `R$${Number(valor).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
    });

    valorMax.addEventListener('input', () => {
        let valor = valorMax.value.replace(/\D/g, '');

        if (!valor) {
            valorMax.value = 'R$0,00';
            return;
        }

        valor = (parseInt(valor, 10) / 100).toFixed(2);
        valorMax.value = `R$${Number(valor).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
    });

    valorEdit.addEventListener('input', () => {
        let valor = valorEdit.value.replace(/\D/g, '');

        if (!valor) {
            valorEdit.value = 'R$0,00';
            return;
        }

        valor = (parseInt(valor, 10) / 100).toFixed(2);
        valorEdit.value = `R$${Number(valor).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
    });

    function limparString(input) {
      return input.trim().replace(/,\s*$/, '');
    }

    function exibirToastError(mensagem) {
        notyf.error(mensagem);
    }

    function exibirToastSuccess(mensagem) {
        notyf.success(mensagem);
    }