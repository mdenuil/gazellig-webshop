#!/bin/bash -e
SECRET_DIR=/run/secrets
mkdir -p "${SECRET_DIR}"

# Add all secrets from the environment variables here
SECRET_NAMES=( EXAMPLE_SECRET )

echo "Moving secrets '${SECRET_NAMES}' to '${SECRET_DIR}' ..."
for secret_name_in_env in ${SECRET_NAMES[*]}; do
    secret=${!secret_name_in_env}
    if [ ! -z "${secret}" ]; then
        echo -en "${secret}" > "${SECRET_DIR}/${secret_name_in_env}"
    else
        echo "${secret_name_in_env} environment variable not set"
    fi
done

echo "Unsetting environment variables '$(ls ${SECRET_DIR})' ..."
unset $(ls "${SECRET_DIR}")

echo 'Starting application ...'
nginx -g 'daemon off;'