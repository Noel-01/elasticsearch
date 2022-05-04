# elasticsearch

Se necesita una imagen de Elasticsearch versión 7.12.1 para generar un contenedor.

-> docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.12.1

Para comprobar que ha levantado bien ir a la url: localhost:9200
