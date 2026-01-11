(ns frontend.modules.instrumentation.posthog
  (:require [frontend.config :as config]
            [frontend.util :as util]
            [frontend.mobile.util :as mobile-util]
            [frontend.version :refer [version]]
            [cljs-bean.core :as bean]))

(goog-define POSTHOG-TOKEN "")
(def ^:const masked "masked")

(defn register []
  ;; Analytics disabled - PostHog register is a no-op
  nil)

(def config
  {:api_host "https://app.posthog.com"
   :persistence "localStorage"
   :autocapture false
   :disable_session_recording true
   :mask_all_text true
   :mask_all_element_attributes true
   :loaded (fn [_] (register))})

(defn init []
  ;; Analytics disabled - PostHog initialization is a no-op
  nil)

(defn opt-out [opt-out?]
  ;; Analytics disabled - PostHog opt-out is a no-op
  nil)

(defn capture [id data]
  ;; Analytics disabled - PostHog capture is a no-op
  nil)

(comment
  (posthog/debug))
