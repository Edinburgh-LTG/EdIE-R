#!/bin/sh
usage="${usage}"
descr="${description}"

. `dirname $0`/../scripts/setup

<#list options as opt>
${opt.opt}=''
</#list>
while [ $# -gt 0 ]; do
    <#noparse>
    arg=$1
    shift
    case $arg in
    </#noparse>
    <#list options as opt>
        -${opt.opt})
        <#if opt.hasArg()>
        ${opt.opt}<#noparse>=$1</#noparse>
        shift
        <#else>
        ${opt.opt}='-${opt.opt}'
        </#if>
        ;;
    </#list>
        <#noparse>
        *)
        echo "usage: $usage" >&2
        exit 2
        </#noparse>
    esac
done

<#list options as opt>
<#if opt.required>
if [ "$${opt.opt}" == "" ]
<#noparse>
then
    echo "usage: $usage" >&2
    exit 2
fi
</#noparse>
</#if>

</#list>

lib=$here/${lib}

<#noparse>
classpath=$lib/bin$(find $here/lib/java -name "*.jar" | sed -n '/.*jar$/ s//:&/p' | tr -d '\n')
</#noparse>

$LOCAL_JAVA -Xms512m -Xmx1024m -classpath "$classpath" ${className} ${cmdopts} > $tmp-javaout 2> $tmp-javaerr || { cat $tmp-javaerr >&2; exit 1; }
