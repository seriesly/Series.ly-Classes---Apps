# -*- coding: utf-8 -*-

## @mainpage Sobre Ally
#
#  @section intro_sec INTRODUCCIÓN
#  Ally es una librería en python que facilita la interacción con la
#  página web http://series.ly<br>
#  Soporta gran cantidad de funciones para interactuar con la web, como
#  por ejemplo mostrar los amigos de un usuario, las películas que se
#  están siguiendo, las que llevas vistas, etc.
#
#  @section Cómo usar la librería
#  Para usar la librería basta con bajarlos los ficheros Ally, Usuario
#  Pelicula, Ficha, Comentario, Serie y dejarlos en el mismo directorio
#  de tu proyecto e importar el fichero Ally.
#
#  @section LICENCIA
#  This program is free software; you can redistribute it and/or
#  modify it under the terms of the GNU General Public License as
#  published by the Free Software Foundation; either version 2 of
#  the License, or (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful, but
#  WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
#  General Public License for more details at
#  http://www.gnu.org/copyleft/gpl.html
#
#  @section NOTAS
#  Con la librería se distribuye también un archivo llamado test.py
#  que muestra de manera simple como se puede usar la librería.<br>
#  Antes de ejecutar test.py necesitas modificar las variables:
#  usuario, contrasena, id_aplicacion y key_aplicacion con
#  información correcta.
#
#  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
#
#  @version 0.80
#
#  @todo Implementar toggle_noti
#  @todo Solucionar problemas con algunas funciones ¿Problema de la Libreria o de la API en sí? (marcar_estado, cambiar_estado etc).
#  @todo Revisar documentación

import urllib, urllib2
import json
import Usuario, Serie, Pelicula, Ficha, Comentario
from xml.dom.minidom import parseString

    ## Clase que proporciona los metodos para acceder a las 
    #  funcionalidades de series.ly.
    #
    #  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
    #  @version 0.80
    #  @section LICENCIA
    #  This program is free software; you can redistribute it and/or
    #  modify it under the terms of the GNU General Public License as
    #  published by the Free Software Foundation; either version 2 of
    #  the License, or (at your option) any later version.
    #
    #  This program is distributed in the hope that it will be useful, but
    #  WITHOUT ANY WARRANTY; without even the implied warranty of
    #  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    #  General Public License for more details at
    #  http://www.gnu.org/copyleft/gpl.html 

class Ally:
       
    ## Constructor de la clase.
    #
    #  El constructor llama a la función privada autenticación
    #  para obtener el token de autenticación y posteriomente
    #  llama a la función privada login para obtener el
    #  token de usuario.
    #
    #  @param usuario usuario que se va a autenticar
    #  @param contrasena contraseña del usuario a autenticar
    #  @param idd ID de la aplicación
    #  @param key Key de la aplicación   
    
    def __init__(self, usuario, contrasena, idd, key):
        ## Variable donde se guarda el token de autenticación.        
        self.auth_token = self._autenticacion(idd, key)
        ## Variable donde se guarda el token de usuario.
        self.user_token = self._login(usuario, contrasena, self.auth_token)
    
    
    ## Método para convertir XML en texto legible.
    #
    #  @param usuario_data Recibe el texto XML en bruto
    #
    #  @return Devuelve un diccionario con todos los campos de información del usuario
    #
    #  @deprecated Esta función está obsoleta puesto que 
    #  ahora la información se recibe por JSON
    
    def parseUsuario(self,usuario_data):
        usuario = {}
        #Añadimos nick
        dom = parseString(usuario_data)
        xmlTag = dom.getElementsByTagName('nick')[0].toxml()
        xmlData=xmlTag.replace('<nick>','').replace('</nick>','')
        usuario['nick'] = xmlData
        #Añadimos uid
        xmlTag = dom.getElementsByTagName('uid')[0].toxml()
        xmlData=xmlTag.replace('<uid>','').replace('</uid>','')
        usuario['uid'] = xmlData
        #Añadimos points
        xmlTag = dom.getElementsByTagName('points')[0].toxml()
        xmlData=xmlTag.replace('<points>','').replace('</points>','')
        usuario['points'] = xmlData
        #Añadimos invitation
        xmlTag = dom.getElementsByTagName('invitations')[0].toxml()
        xmlData=xmlTag.replace('<invitations>','').replace('</invitations>','')
        usuario['invitations'] = xmlData
        #Añadimos badge
        xmlTag = dom.getElementsByTagName('badges')[0].toxml()
        badge = parseString(xmlTag.encode('ascii', 'ignore'))
        xmlBadge = badge.getElementsByTagName('item')
        usuario['badges'] = {}
        for i in xmlBadge:
            item = i.toxml()
            item = parseString(item)
            nombre = item.getElementsByTagName('name')[0].toxml()
            xmlData=nombre.replace('<name>','').replace('</name>','')
            usuario['badges']['nombre'] = xmlData
            descipcion = item.getElementsByTagName('description')[0].toxml()
            xmlData=descipcion.replace('<description>','').replace('</description>','')
            usuario['badges']['descipcion'] = xmlData
        #Añadimos los amigos
        xmlTag = dom.getElementsByTagName('friends')[0].toxml()
        friends = parseString(xmlTag.encode('ascii', 'ignore'))
        xmlFriends = friends.getElementsByTagName('item')
        usuario['friends'] = []
        for i in xmlFriends:
            amigo = {}
            item = i.toxml()
            item = parseString(item)
            #Añadimos el nick del amigo
            nick = item.getElementsByTagName('nick')[0].toxml()
            xmlData=nick.replace('<nick>','').replace('</nick>','')
            amigo['nick'] = xmlData
            #Añadimos el uid del amigo
            uid = item.getElementsByTagName('uid')[0].toxml()
            xmlData=uid.replace('<uid>','').replace('</uid>','')
            amigo['uid'] = xmlData
            #Añadimos el avatar del amigo
            avatar = item.getElementsByTagName('avatar')[0].toxml()
            xmlData=avatar.replace('<avatar>','').replace('</avatar>','')
            amigo['avatar'] = xmlData
            usuario['friends'].append(amigo)
        #Añadimos los followers
        xmlTag = dom.getElementsByTagName('followers')[0].toxml()
        followers = parseString(xmlTag.encode('ascii', 'ignore'))
        xmlFollowers = followers.getElementsByTagName('item')
        usuario['followers'] = []
        for i in xmlFollowers:
            follow = {}
            item = i.toxml()
            item = parseString(item)
            #Añadimos el nick del follow
            nick = item.getElementsByTagName('nick')[0].toxml()
            xmlData=nick.replace('<nick>','').replace('</nick>','')
            follow['nick'] = xmlData
            #Añadimos el uid del follow
            uid = item.getElementsByTagName('uid')[0].toxml()
            xmlData=uid.replace('<uid>','').replace('</uid>','')
            follow['uid'] = xmlData
            #Añadimos el avatar del follow
            avatar = item.getElementsByTagName('avatar')[0].toxml()
            xmlData=avatar.replace('<avatar>','').replace('</avatar>','')
            follow['avatar'] = xmlData
            usuario['followers'].append(follow)
        #print xmlu
        return usuario
    
    ## Método empleado para realizar la comunicación con la web.
    #
    #  @param metodo Tipo de solicitud que se le hace a la web
    #  @param parametros Parametros que se le pasan.
    #  @param url Dirección a la que hacer la petición
     
    def comunicacion(self, metodo, parametros, url):
        #print url + "/" + metodo + "?" + parametros
        respuesta = urllib2.urlopen(url + '/' + metodo + '?' + parametros)
        return respuesta.read()
    
    ## Método para realizar la autenticación.
    #
    #  @param idd ID de la aplicación
    #  @param key KEY de la aplicación
    #
    #  @return Devuelve un token de autenticación  
    
    def _autenticacion(self, idd, key):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({"api" : idd, "secret" : key})
        return self.comunicacion("auth.php", parametros, url)
    
    ## Método para realizar el login.
    #
    #  @param usuario Usuario que se quiere loguear
    #  @param contrasena Contraseña del usuario a loguear
    #  @param auth_token Token de autenticación recibido en el método_autenticacion   
    #
    #  @return Devuelve un token de usuario
    
    def _login(self, usuario, contrasena, auth_token):
        url = "http://series.ly/scripts/login/login.php"
        parametros = urllib.urlencode({'lg_login' : usuario, 'lg_pass' : contrasena, 'callback_url' : 'no', 'auth_token' : auth_token, 'autologin' : ''})
        peticion = urllib2.Request(url, parametros)
        respuesta = urllib2.urlopen(peticion) # This request is sent in HTTP POST
        datos = respuesta.read()
        return datos
    
    ## Método para consultar la información de un usuario.
    #
    #  Método para consultar la información de un usuario,
    #  si el uid no se especifica entonces devuelve la información
    #  del usuario que hace la llamada al método.
    #
    #  @param uid UID del usuario a consultar, si se deja en blanco, se consulta la información del usuario
    #  que hace la llamada al método.
    #
    #  @return Devuelve un objeto Usuario, exactamente con la siguiente información:
    #  nick, uid, points, invitations, avatar, badges, friends y followers. En el caso
    #  de que se produzca un error devuelve None.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def user_info(self, uid = ""):
        url = "http://series.ly/api"
        if uid == "":
            parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'format' : 'json'})
        else:
            parametros = urllib.urlencode({'auth_token': self.auth_token, 'uid' : uid, 'format' : 'json'})
        datos = self.comunicacion("userInfo.php", parametros, url)
        try:
            respuesta = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        return Usuario.Usuario(respuesta['nick'], respuesta['uid'], respuesta['points'], respuesta['invitations'], respuesta['avatar'], respuesta['badges'], respuesta['friends'], respuesta['followers'])

        
    
    ## Método que devuelve las series del usuario autenticado.
    #
    #  @return Devuelve una lista de objetos Serie con la siguiente información:
    #  idSerie, title, seasons, episodes, poster, thumb, small_thumb, status, self(una referencia a Ally)
    #  para poder llamar a la función getCapitulos desde los objetos Serie.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def user_misSeries(self):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'format' : 'json'})
        datos = self.comunicacion("userSeries.php", parametros, url)
        try:
            respuesta = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        misSeries = []
        for i in respuesta:
            misSeries.append(Serie.Serie(i['idSerie'], i['title'], i['seasons'], i['episodes'], i['poster'], i['thumb'], i['small_thumb'], i['status'], self))
        return misSeries
    
    ## Método que devuelve las películas del usuario autenticado.
    #
    #  @return Devuelve una lista de objetos Pelicula con la siguiente información:
    #  idFilm, title, year, genre, poster, thumb, small_thumb, status.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def user_misPelis(self):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'format' : 'json'})
        datos = self.comunicacion("userMovies.php", parametros, url)
        try:
            respuesta = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        misPelis = []
        for i in respuesta:
            misPelis.append(Pelicula.Pelicula(i['idFilm'], i['title'], i['year'], i['genre'], i['poster'], i['thumb'], i['small_thumb'], i['status']))
        return misPelis
    
    ## Método que devuelve si el usuario sigue conectado o no.
    #
    #  @return Devuelve 1 si el usuario sigue conectado y 0 en caso contrario.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def user_estado(self):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token})
        datos = self.comunicacion("isUserLogged.php", parametros, url)
        return datos
    
    ## Método que devuelve las películas y series pendientes por ver
    #
    #  @return Devuelve 2 lista de objetos, una de objetos Serie y otra de objetos Pelicula,
    #  Los objeto Serie contienen la siguiente información: idSerie, title
    #  el capítulo que toca por ver, el número del capítulo que toca por ver y el estado pendiente.
    #  Los objetos Pelicula contienen la siguiente información: idFilm, title y el estado pendiente.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def user_notification(self):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'format' : 'json'})
        datos = self.comunicacion("mySeriesly.php", parametros, url)
        try:
            respuesta = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        pelisPendientes = []
        seriesPendientes = []
        for i in respuesta['films']:
            pelisPendientes.append(Pelicula.Pelicula(i['id_peli'], i['nom_peli'], status='pending'))
        for i in respuesta['series']:
            seriesPendientes.append(Serie.Serie(idSerie=i['id_serie'], title=i['nom_serie'], idc=i['idc'], num=i['num'], status='pending'))
        return pelisPendientes, seriesPendientes
    
    ## Método que devuelve las notificaciones del usuario.
    #
    #  @return Devuelve las notificaciones del usuario.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def notis_list(self):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'format' : 'json'})
        datos = self.comunicacion("notislist.php", parametros, url)
        try:
            return json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None

    def toggle_noti(self):
        #TODO No se puede realizar, puesto que en notis_list no devuelve la id de la notificacion
        print "hola"
    
    ## Método para buscar un termino de un tipo concreto.
    #
    #  Método para buscar un termino, siguiendo un tipo concreto (film, serie, user o all).
    #
    #  @param termino termino que se quiere buscar.
    #  @param tipo Tipo de recurso que se quiere buscar (film, serie, user o all).
    #
    #  @return Devuelve una serie de listas dependiendo del tipo elegido.
    #  Si tipo=film, devuelve una lista de objetos Pelicula con la siguiente información:
    #  idFilm, title, year, genre, poster, thumb, small_thumbs.
    #  Si tipo=serie, devuelve una lista de objetos Serie con la siguiente información:
    #  idSerie, title, seasons, episodes, poster, thumb, small_thumb.
    #  Si tipo=user devuelve una lista de objetos Usuario con la siguiente información:
    #  nick, uid, nombre, avatar.
    #  Si tipo=all, devuelve un diccionario con 3 claves "serie, peli y user" y por cada
    #  clave una lista que contiene objetos Serie, Pelicula y Usuario respectivamente con
    #  la información detallada anteriormente.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def buscar(self, termino, tipo):
        #tipo = film, serie, user, all
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'search' : termino, 'type' : tipo, 'format' : 'json'})
        datos = self.comunicacion("search.php", parametros, url)
        try:
            respuesta = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        if (tipo != 'all'):
            devolver = []
            for i in respuesta:
                if (tipo =='film'):
                    devolver.append(Pelicula.Pelicula(i['idFilm'], i['title'], i['year'], i['genre'], i['poster'], i['thumb'], i['small_thumb']))
                elif (tipo == 'serie'):
                    devolver.append(Serie.Serie(i['idSerie'], i['title'], i['seasons'], i['episodes'], i['poster'], i['thumb'], i['small_thumb']))
                else:
                    devolver.append(Usuario.Usuario(i['nick'], i['idUser'], nombre=i['name'], avatar=i['poster']))
        else:
            devolver = {}
            devolver['serie'] = []
            for i in respuesta['serie']:
                devolver['serie'].append(Serie.Serie(i['idSerie'], i['title'], i['seasons'], i['episodes'], i['poster'], i['thumb'], i['small_thumb']))
            devolver['peli'] = []
            for i in respuesta['peli']:
                devolver['peli'].append(Pelicula.Pelicula(i['idFilm'], i['title'], i['year'], i['genre'], i['poster'], i['thumb'], i['small_thumb']))
            devolver['user'] = []
            for i in respuesta['user']:
                devolver['user'].append(Usuario.Usuario(i['nick'], i['idUser'], nombre=i['name'], avatar=i['poster']))
        return devolver
    
    ## Método que devuelve la ficha de una película concreta.
    #
    #  @param idFilm ID de la película que se puede encontrar en la busqueda.
    #
    #  @return Devuelve un objeto Ficha con información sobre la película, concretamente con la siguiente información:
    #  title, idd, synopsis, seriesly_score, participants_score, poster, thumb, small_thumb, year y links.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
     
    def ficha_peli(self, idFilm):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'idFilm' : idFilm, 'format' : 'json'})
        datos = self.comunicacion("detailMovie.php", parametros, url)
        try:
            resp = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        return Ficha.Ficha(resp['title'], resp['idp'], resp['synopsis'], resp['seriesly_score'], resp['participants_score'], resp['poster'], resp['thumb'], resp['small_thumb'], resp['year'], resp['links'])
    
    ## Método que devuelve la ficha de una serie concreta.
    #
    #  @param idSerie ID de la serie que se puede encontrar en la busqueda.
    #
    #  @return Devuelve un objeto Ficha con ifnromación sobre la serie, concretamente la siguiente información:
    #  title, idd, synopsis, seriesly_score, participants_score, poster, thumb, small_thumb, self (una referencia a esta clase 
    #  para poder usar el método getCapitulos) y idSerie.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def ficha_serie(self, idSerie):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'idSerie' : idSerie, 'format' : 'json'})
        datos = self.comunicacion("detailSerie.php", parametros, url)
        try:
            resp = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        return Ficha.Ficha(resp['title'], resp['ids'], resp['synopsis'], resp['seriesly_score'], resp['participants_score'], resp['poster'], resp['thumb'], resp['small_thumb'], ally=self, idSerie=idSerie)
    
    #Aqui user_token puede ser vacio, si es vacio no se muestran que episodios se ha visto
    ## Método que devuelve los capítulos de una serie.
    #
    #  Devuelve los capítulos de una serie, indicando si se han visto o no.
    #
    #  @param idSerie ID de la serie que se puede encontrar en la busqueda
    #
    #  @return Devuelve una lista de capitulos de la serie.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
     
    def caps_serie(self, idSerie):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'idSerie' : idSerie, 'format' : 'json'})
        datos = self.comunicacion("capsSerie.php", parametros, url)
        try:
            return json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
    
    ## Método que devuelve los links de un capítulo en concreto.
    #
    #  @param idCap ID del capítulo, puede ser obtenido en caps_serie.
    #
    #  @return Devuelve una lista de links del capítulo.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def links_cap(self, idCap):
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'idCap' : idCap, 'format' : 'json'})
        datos = self.comunicacion("linksCap.php", parametros, url)
        try:
            return json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
    
    ## Método que muestra el enlace directo de un capítulo.
    #  
    #  @param datos Datos obtenidos en la función links_cap.
    #  @param i Indica que links se quiere cargar de todos los que hay disponibles.
    #
    #  @return Devuelve un links directo para ver el capítulo.
      
    def cargar_enlace(self, datos, i):
        return datos['links'][i]['url']
    
    #Falla de vez en cuando ¿Por qué?
    ## Método para cambiar de estado una película o una serie.
    #
    #  Este método se usa para cambiar de estado una serie o pelicula, por ejemplo:
    #  seguir una serie, poner como vista una película, etc...
    #
    #  @param idd ID de la serie o película.
    #  @param typee Tipo de recurso: 0(serie) o 1(pleícula).
    #  @param status Estado al que se quiere actualizar el recurso:
    #  3(vista), 2(pendiente), 1(siguiendo), -1(quitar de ms series).
    #
    #  @return Dvuelve 1 si todo ha ido bien y 0 en caso contrario.
    
    def cambiar_estado(self, idd, typee, status):
        #Type = 0(serie), 1 (pelicula)
        #Status = 3(vista), 2(pendiente), 1(siguiendo), -1(quitar de ms series)
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'id' : idd, 'type' : typee, 'status' : status, 'format' : 'json'})
        datos = self.comunicacion("changeStatus.php", parametros, url)
        respuesta = json.loads(datos)
        return respuesta
    
    #Falla de vez en cuando ¿Por qué?
    ## Método empleado para marcar un episodio como visto.
    #
    #  @param idCap ID del capitulo se puede conseguir en el método caps_serie.
    #  @param action Acción que quieres realizar: 1(Marcar episodio), 0(Desmarcar Episodio)
    #
    #  @return Devuelve 1 si todo ha ido bien y 0 en caso contrario.
    
    def marcar_episodio(self, idCap, action):
        #action = 1(Marcar episodio), 0(Desmarcar Episodio)
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'idCap' : idCap, 'action' : action, 'format' : 'json'})
        datos = self.comunicacion("episodeToggle.php", parametros, url)
        respuesta = json.loads(datos)
        return respuesta
    
    ## Método para puntuar un determinado recurso.
    #
    #  @param idd ID del recurso que se quiere puntuar
    #  @param tipus Tipo del recurso: 0(serie), 1(pelicula)
    #  @param rating Puntuación a dar (del 1 al 5)
    #
    #  @return Devuelve 1 si todo ha ido bien y 0 en caso contrario.
    
    def puntuar(self, idd, tipus, rating):
        #tipus = 0(serie), 1(pelicula)
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'id' : idd, 'tipus' : tipus, 'rating' : rating, 'format' : 'json'})
        datos = self.comunicacion("rate.php", parametros, url)
        respuesta = json.loads(datos)
        return respuesta['result']
    
    ## Método que devuelve ciertos tops
    #  
    #  @param idd Tipo del recurso que quieremos consultar su top: 
    #   1(series más votadas), 2(Pelis más vistas), 3(Ultimas pelis modificadas)
    #
    #  @return Devuelve distinta información dependiendo del idd que se le pase.
    #  Si idd=1(series más votadas) entonces devolverá una lista de objetos Serie con la siguiente información:
    #  title, idSerie, votos.
    #  Si idd=2(pelis más vistas) entonces devolverá una lista de objetos Pelicula con la siguiente información:
    #  title, idFilm.
    #  Si idd=3(ultimas películas) entonces devolverá una lista de objetos Pelicula con la siguiente información:
    #  title, idFilm, any.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def tops(self, idd):
        #id = 1(series más votadas), 2(Pelis más vistas), 3(Ultimas pelis modificadas)
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'id' : idd, 'format' : 'json'})
        datos = self.comunicacion("top.php", parametros, url)
        try:
            resp = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        devolver = []
        if (idd == "1"):
            for i in resp['series_mes_votades']:
                devolver.append(Serie.Serie(title=i['nom_serie'], idSerie=i['id_serie'], votos=i['vots']))
        elif (idd == "2"):
            for i in resp['pelis_mes_vistes']:
                devolver.append(Pelicula.Pelicula(title=i['nom_peli'], idFilm=i['id_peli']))
        elif (idd == "3"):
            for i in resp['ultimes_pelis']:
                devolver.append(Pelicula.Pelicula(title=i['nom_peli'], idFilm=i['id_peli'], any=i['any']))
        return devolver
    
    ## Método empleado para mostrar los comentarios.
    #
    #  @param idd ID del recurso que se quiere consultar sus comentarios.
    #  @param media Tipo de recurso (user, serie, peli). 
    #
    #  @return Devuelve los comentarios del recurso seleccionado en el parámetro media.
    #
    #  @exception ValueError Se produce una excepción ValueError si los datos no pueden ser decodificados.
    
    def mostrar_comentarios(self, idd, media):
        #media = user, serie, peli
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'id' : idd, 'media' : media, 'format' : 'json'})
        datos = self.comunicacion("mediaComments.php", parametros, url)
        try:
            resp = json.loads(datos)
        except ValueError:
            print "No ha sido posible recibir información"
            return None
        devolver = []
        for i in resp:
            if (len(i) == 5):
                devolver.append(Comentario.Comentario(i['comment'], i['created'], i['author'], i['id'], i['totalVotes']))
            else:
                devolver.append(Comentario.Comentario(i['comment'], i['created'], i['author'], i['id'], i['totalVotes'], i['subcomments']))
        return devolver
    
    #NO PROBADO
    ## Método empleado para puntuar comentarios.
    #
    #  @param idd ID del comentario a puntuar
    #  @param value 1 si se va a votar a favor y 2 en caso contrario.
    #
    #  @return Devuelve 1 si todo ha ido bien.
    
    def puntuar_comentarios(self, idd, value):
        #value = 1 (A favor), 2 (En contra)
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'id' : idd, 'value' : value, 'format' : 'json'})
        datos = self.comunicacion("rateComment.php", parametros, url)
        respuesta = json.loads(datos)
        return respuesta['result']
    
    #Funciona con media=serie pero no con los demás
    ## Método para publicar un nuevo comentario.
    #
    #  @param text Texto del comentario.
    #  @param idd ID del recurso a comentar.
    #  @param media Indica el tipo de recurso (serie, user, peli, subcomment).
    #
    #  @return Devuelve 1 si todo ha ido bien y 0 en caso contrario
    
    def nuevo_comentario(self, text, idd, media):
        #media = serie, user, peli, subcomment
        url = "http://series.ly/api"
        parametros = urllib.urlencode({'auth_token': self.auth_token, 'user_token' : self.user_token, 'text' : text, 'id' : idd, 'media' : media, 'format' : 'json'})
        datos = self.comunicacion("newComment.php", parametros, url)
        respuesta = json.loads(datos)
        return respuesta['result']


