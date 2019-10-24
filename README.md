# Certificado-impressao-e-db

Software de edição e impressão de certificados acompanhado por uma base de dados em SQLite feito em Java com o auxílio da biblioteca JavaFX.

# Introdução

Software criado com o intuito de servir como ponte para impressão de certificados durante um evento anual dos bombeiros de Araras. Para tanto ele comporta uma aba com dados cadastrados divididos por ano em sua pesquisa, bem como uma pesquisa para cada elemento separadamente.

Sua exibição utiliza-se de HTML/CSS e Javascript e serve como base para a impressão mostrando ao usuário exatamente o que vai ser impresso. 

# Instalação

* Extraia os arquivos do repositório na pasta desejada.
* Baixe a versão mais atual do sqlite-jdbc: https://bitbucket.org/xerial/sqlite-jdbc/downloads/ (Ultima versão testada: 3.27.2.1)
* Abra o projeto via Eclipse em "Open Project from File System...".
* Vá em File>Properties>Java Build Path>Libraries, clique no arquivo .jar do SQLite, clique em "Edit..." e indique o caminho do drive SQLite.
