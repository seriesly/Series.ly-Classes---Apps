# -*- coding: utf-8 -*-

#Archivo de prueba, para probar la librería
#para que funcione hay que modificar las 
#variables usuario, contrasena, id_aplicacion
#y key_aplicacion con información adecuada

import Ally

usuario = ""
contrasena = ""
id_aplicacion = ""
key_aplicacion = ""

print "Iniciando la API..."
Api = Ally.Ally(usuario,contrasena,id_aplicacion,key_aplicacion)
print "API iniciada!"

print "Cargando información del usuario..."
usuario = Api.user_info()
print "Nick: " + usuario.getNick()
print "UID: " + usuario.getUid()
print "Avatar: " + usuario.getAvatar()
print "Amigos: "
amigos = usuario.getFriends()
for i in amigos:
    print i.getNick()
print "Información del usuario cargada!"

print "Cargando Mis Series"
misSeries = Api.user_misSeries()
for i in misSeries:
    print i.getTitle()
print "Series Cargadas!"

print "Cargando Mis Películas..."
misPelis = Api.user_misPelis()
for i in misPelis:
    print i.getTitle()
print "Películas Cargadas!"

print "¿Estoy conectado?"
print Api.user_estado()

print "Obteniendo capítulos de " + misSeries[0].getTitle()
print misSeries[0].getCapitulos()
print "Buscando la palabra harry en usuarios, peliculas y series"
resultado = Api.buscar("Harry", "all")
print "Buscando en series"
for i in resultado['serie']:
    print i.getTitle()
print "Buscando en películas..."
for i in resultado['peli']:
    print i.getTitle()
print "Buscando en usuarios"
for i in resultado['user']:
    print i.getNick()
    
print "Cargando ficha de la primera película obtenida en la lista: " + resultado['peli'][0].getTitle()
fichaPeli = Api.ficha_peli(resultado['peli'][0].getIdFilm())
print fichaPeli.getSynopsis()
print "Ficha cargada!"

print "Cargando ficha de la primera serie obtenida en la lista: " + resultado['serie'][0].getTitle()
fichaSerie = Api.ficha_serie(resultado['serie'][0].getIdSerie())
print fichaSerie.getSynopsis()
print "Ficha cargada!"

print "Obteniendo Links de la película" + resultado['peli'][0].getTitle()
print fichaPeli.getLinks()
print "Links Obtenidos!"

print "Obteniendo links de la serie EJEMPLO DE QUE HAY QUE TENER CUIDADO CON LOS MÉTODOS A LOS QUE SE LLAME"
print fichaSerie.getLinks()
print "No se debe llamar a esta función con series :( por que devuelve NONE"

print "Obteniendo notificaciones de series y películas"
pelis, series = Api.user_notification()
for i in pelis:
    print i.getTitle()
for i in series:
    print i.getTitle() + " siguiente capítulo a ver: " + i.getNum()
print "Notificaciones obtenidas"

print "Obteniendo el TOP de las últimas películas"
ultimas = Api.tops("3")
for i in ultimas:
    print i.getTitle()

print "Obteniendo series más votadas"
votadas = Api.tops("1")
for i in votadas:
    print "" + i.getTitle() + " Votos: " + i.getVotos()
    
print "Mostrando tus propios comentarios"
comentarios = Api.mostrar_comentarios(usuario.getUid(), "user")
for i in comentarios:
    print i.getComment() + " de " + i.getAuthor()

print "Obteniendo Link del primer capítulo de " + misSeries[0].getTitle()
print Api.links_cap(misSeries[0].getCapitulos()[0]['idc'])

#print Api.cambiar_estado(misPelis[0].getIdFilm(), 1, 3) #no va
print "Marcando episodio " + misSeries[0].getCapitulos()[5]['season'] + " de " + misSeries[0].getTitle()
print Api.marcar_episodio(misSeries[0].getCapitulos()[5]['idc'], "1") #NO VA

print "Puntuando " + misSeries[0].getTitle()
print Api.puntuar(misSeries[0].getIdSerie, "1", "5") #NO VA

print "Comentando a " + usuario.getFriends()[0].getNick()
print Api.nuevo_comentario("Prueba", usuario.getFriends()[0].getUid(), 'user' ) #Error Media not Found
