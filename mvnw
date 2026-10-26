#!/bin/bash
# Wrapper portátil para execução do Maven no ambiente local e IntelliJ

BASEDIR=$(dirname "$0")

# Localiza Java
if [ -z "$JAVA_HOME" ]; then
    if [ -x "/var/lib/flatpak/app/com.jetbrains.IntelliJ-IDEA-Community/current/active/files/jbr/bin/java" ]; then
        export JAVA_HOME="/var/lib/flatpak/app/com.jetbrains.IntelliJ-IDEA-Community/current/active/files/jbr"
    fi
fi

# Localiza Maven
if command -v mvn >/dev/null 2>&1; then
    MVN_CMD="mvn"
elif [ -x "/var/lib/flatpak/app/com.jetbrains.IntelliJ-IDEA-Community/current/active/files/plugins/maven-plugin/lib/maven3/bin/mvn" ]; then
    MVN_CMD="/var/lib/flatpak/app/com.jetbrains.IntelliJ-IDEA-Community/current/active/files/plugins/maven-plugin/lib/maven3/bin/mvn"
elif [ -x "/tmp/bin/mvn" ]; then
    MVN_CMD="/tmp/bin/mvn"
else
    echo "Erro: Maven não encontrado. Instale o Maven ou certifique-se de que o IntelliJ esteja instalado."
    exit 1
fi

exec "$MVN_CMD" "$@"
