#!/bin/bash 

function usage() 
{
    echo " Usage : "
    exit 0
}

    case $1 in
    applyTransaction)
            [ $# -lt 3 ] && { usage; }
            ;;
    postTransaction)
            [ $# -lt 4 ] && { usage; }
            ;;
    queryTransaction)
            [ $# -lt 2 ] && { usage; }
            ;;
    decryptTransaction)
            [ $# -lt 3 ] && { usage; }
            ;;
 authorize)
            [ $# -lt 3 ] && { usage; }
            ;;
    *)
        usage
            ;;
    esac

    java -Djdk.tls.namedGroups="secp256k1" -cp 'apps/*:conf/:lib/*' org.fisco.bcos.asset.client.AssetClient $@
