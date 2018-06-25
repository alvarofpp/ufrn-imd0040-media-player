package ufrn.alvarofpp.controllers;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import ufrn.alvarofpp.PlayerP;
import ufrn.alvarofpp.db.models.User;
import ufrn.alvarofpp.ui.MediaPlayerUI;
import ufrn.alvarofpp.controllers.helpers.Coordinates;
import ufrn.alvarofpp.controllers.helpers.AnimationGenerator;
import ufrn.alvarofpp.db.files.Musics;
import ufrn.alvarofpp.db.models.Music;

public class MediaPlayerController extends DefaultController {
    /**
     * Interface de usuário
     */
    @FXML
    private HBox mediaplayerui;
    /**
     * Interface ao lado
     */
    @FXML
    private Pane sidebar;
    /**
     * Label de nome de usuário, nome de playlist, nome da música
     */
    @FXML
    private Label usernameLabel, playlistLabel, musicLabel;
    /**
     * Label de nome de usuário, nome de playlist, nome da música
     */
    @FXML
    private ListView musicList, playlistList;
    /**
     * Animações
     */
    private AnimationGenerator animationGenerator;

    /**
     * Usuário logado
     */
    private User user;
    Musics musics = new Musics();
    private boolean press = true;
    
    
    PlayerP player = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        coordinates = new Coordinates();
        makeStageDrageable();
        animationGenerator = new AnimationGenerator();

        // Testes
        this.user = new User("alvarofpp", "alvaro123");
        this.prepareEnv();
    }
    
    @FXML
    private void handlePlay(){
        if(player == null){
            player = new PlayerP(musics.nextMusic().getPath());
        }
        
        if(press == true){
            player.Play();
            press = false;
        }else{
            player.Pause();
            press = true;
        }
    }
    
    @FXML
    private void handleNextMusic(){
       Music music = musics.nextMusic();
        if( music!=null ){
            player.Pause();
            player = new PlayerP(music.getPath());
            System.out.println(music.getPath());
        }
     }
    
    @FXML
    private void handleBackMusic(){
        Music music = musics.backMusic();
        if( music!=null ){
            System.out.println(music.getPath());
            player.Pause();;
            player = new PlayerP(music.getPath());
        } 
    }
    /**
     * Prepara o ambiente da view
     */
    private void prepareEnv() {
        // Nome do usuário
        this.usernameLabel.setText(this.user.getUsername());
        // Playlist inicialmente é null
        this.playlistLabel.setText(null);
        // Música inicialmente é null
        this.musicLabel.setText("Não está tocando nada");
        // Upload das músicas
    }

    /**
     * Mostra o sidebar
     * @param event
     */
    @FXML
    private void showSidebar(MouseEvent event) {
        if (!sidebar.isVisible()) {
            sidebar.setVisible(true);
            animationGenerator.applyTranslateAnimationOn(sidebar);
            animationGenerator.applyFadeAnimationOn(sidebar, AnimationGenerator.INVISIBLE, AnimationGenerator.VISIBLE, null);
        } else {
            animationGenerator.applyFadeAnimationOn(sidebar, AnimationGenerator.VISIBLE, AnimationGenerator.INVISIBLE, (e) -> {
                sidebar.setVisible(false);
            });
        }
    }

    /**
     * Torna a interface arrastavel.
     */
    @Override
    protected void makeStageDrageable() {
        mediaplayerui.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                coordinates.setxOffset(event.getSceneX());
                coordinates.setyOffset(event.getSceneY());
            }
        });
        mediaplayerui.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MediaPlayerUI.stage.setX(event.getScreenX() - coordinates.getxOffset());
                MediaPlayerUI.stage.setY(event.getScreenY() - coordinates.getyOffset());
                MediaPlayerUI.stage.setOpacity(0.7f);
            }
        });
        mediaplayerui.setOnDragDone((e) -> {
            MediaPlayerUI.stage.setOpacity(AnimationGenerator.VISIBLE);
        });
        mediaplayerui.setOnMouseReleased((e) -> {
            MediaPlayerUI.stage.setOpacity(AnimationGenerator.VISIBLE);
        });
    }

    public void setUser(User user) {
        this.user = user;
    }
}
