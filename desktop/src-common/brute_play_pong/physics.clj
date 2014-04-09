(ns ^{:doc "Physics system"}
    brute-play-pong.physics
    (:use [play-clj.core]
          [play-clj.math])
    (:require [brute.entity :as e])
    (:import [brute_play_pong.component Rectangle Ball Velocity]))

(defn- ^Boolean touching-left-wall
    [^com.badlogic.gdx.math.Rectangle geom]
    (let [geom-x (rectangle! geom :get-x)]
        (< geom-x 0)))

(defn- ^Boolean touching-right-wall
    [^com.badlogic.gdx.math.Rectangle geom]
    (let [geom-x (rectangle! geom :get-x)
          geom-width (rectangle! geom :get-width)
          screen-width (graphics! :get-width)]
        (> (+ geom-x geom-width) screen-width)))

(defn- keep-rects-in-world
    []
    (doseq [entity (e/get-all-entities-with-component Rectangle)]
        (let [rect (e/get-component entity Rectangle)
              colour (:colour rect)
              geom (:rect rect)]
            (when (touching-left-wall geom)
                (rectangle! geom :set-x 0))
            (when (touching-right-wall geom)
                (rectangle! geom :set-x (- (graphics! :get-width) (rectangle! geom :get-width)))))))

(defn- move-ball
    [delta]
    (doseq [entity (e/get-all-entities-with-component Ball)]
        (let [vel (:vec (e/get-component entity Velocity))
              rect (:rect (e/get-component entity Rectangle))
              rect-x (rectangle! rect :get-x)
              rect-y (rectangle! rect :get-y)]
            (rectangle! rect :set-x (+ rect-x (* (.x vel) delta)))
            (rectangle! rect :set-y (+ rect-y (* (.y vel) delta)))

            (when (or (touching-left-wall rect) (touching-right-wall rect))
                (println "Touching!" rect vel)
                (set! (.x vel) (* -1 (.x vel))))

            )
        ))



(defn process-one-game-tick
    "Physics, process one game tick"
    [delta]
    (move-ball delta)
    (keep-rects-in-world))
