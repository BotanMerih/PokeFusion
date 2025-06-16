# PokeFusion Modu

Bu mod, Minecraft sunucunuza Pixelmon'lar arasında bir füzyon mekaniği ekler. Oyuncular, iki Pokémon'u birleştirerek birinin özelliklerini diğerine aktarabilirler. Mod, hem komutlarla hem de (isteğe bağlı) bir grafiksel kullanıcı arayüzü (GUI) ile çalışır.

## Özellikler

*   **Pokémon Füzyonu:** İki Pokémon'u birleştirerek temel Pokémon'un istatistiklerini güçlendirin.
*   **Kullanıcı Arayüzü:** Füzyon işlemleri için kullanımı kolay bir GUI.
*   **Komut Desteği:** GUI kullanmak istemeyenler veya otomasyon için komut tabanlı kontrol.
*   **Ekonomi Entegrasyonu:** Füzyon işlemleri için sunucunuzdaki ekonomi eklentileriyle (Vault aracılığıyla herhangi biri) entegrasyon.
*   **Esnek Yapılandırma:** Füzyon kurallarını, maliyetlerini ve özellik transferini tamamen yapılandırın.

## Kullanım

Füzyon işlemini başlatmak için iki yol vardır:

1.  **Komutlar:**
    *   `/pokefusion`: Mevcut seçili Pokémon'ları gösterir.
    *   `/pokefusion 1 <parti slotu>`: Füzyonun temeli olacak Pokémon'u seçer (partinizdeki 1-6 arası bir sayı).
    *   `/pokefusion 2 <parti slotu>`: Özelliklerini feda edecek Pokémon'u seçer.
    *   `/pokefusion confirm`: Seçilen Pokémon'lar ile füzyonu onaylar ve gerçekleştirir.
    *   `/pokefusion decline`: Seçimi iptal eder.
    *   `/pokefusion help`: Yardım komutlarını gösterir.



## Yapılandırma

Modun tüm ayarları, sunucunuzun `config` klasöründeki `fusions-common.toml` dosyasından yönetilir.

### Genel Ayarlar (`General Settings`)
*   `EnableGUI`: Füzyon için grafiksel arayüzü açar/kapatır. (Varsayılan: `true`)BOZUK BOZUK BOZUK
*   `CooldownSeconds`: `/pokefusion` komutu için saniye cinsinden bekleme süresi. (Varsayılan: `30`)
*   `FusionCost`: Her bir füzyon işleminin maliyeti. (Varsayılan: `1000.0`)
*   `FusionItem`: Füzyon için gerekli olan eşya. (Örn: `"minecraft:diamond"`. Boş bırakırsanız devre dışı kalır.)
*   `EconomyCommand`: Oyuncudan para çekmek için çalıştırılacak komut.
    *   **{player}**: Oyuncunun adını temsil eder.
    *   **{cost}**: Füzyon maliyetini temsil eder.
    *   Varsayılan (Vault uyumlu): `"eco take {player} {cost}"`

### Füzyon Kuralları (`Fusion Rules`)
*   `FusionMode`: Hangi Pokémon'ların birbiriyle birleşebileceğini belirler (`SPECIES_ONLY`, `EGG_GROUP`, `ANY`).
*   `MaxFuseCount`: Bir Pokémon'un maksimum kaç kez füzyonlanabileceği. (`-1` sınırsız demektir).

### Özellik Transfer Ayarları (`Transfer Settings`)
Bu ayarlar, feda edilen Pokémon'dan temel Pokémon'a hangi özelliklerin ne oranda geçeceğini belirler.
*   `ivGainPercentage`: Feda edilen Pokémon'un IV'lerinden ne kadarının kazanılacağı (yüzde olarak).
*   `inheritShiny`: Eğer feda edilen Pokémon "shiny" ise, temel Pokémon'un da "shiny" olup olmayacağı.
*   `inheritHiddenAbility`: Gizli yetenek aktarımı.
*   ...ve diğer istatistik transfer ayarları.

## Kurulum

1.  Sunucunuzda Minecraft Forge'un uygun sürümünün yüklü olduğundan emin olun.
2.  Pixelmon Modunun son sürümünü `mods` klasörünüze yükleyin.
3.  Bu modun `.jar` dosyasını sunucunuzun `mods` klasörüne atın.
4.  Sunucuyu başlatın. İlk başlatmanın ardından `config/fusions-common.toml` dosyası oluşacaktır.

## Geliştiriciler İçin Kurulum (Kaynak Koddan)

Bu proje Minecraft Forge MDK (Mod Development Kit) yapısını kullanır.

1.  Terminali/komut satırını proje klasöründe açın.
2.  Tercih ettiğiniz IDE'ye göre kurulum yapın:
    *   **Eclipse:** `gradlew genEclipseRuns` komutunu çalıştırın ve ardından projeyi Eclipse'e "Existing Gradle Project" olarak import edin.
    *   **IntelliJ IDEA:** `build.gradle` dosyasını IDEA'da açarak projeyi import edin. Ardından `gradlew genIntellijRuns` komutunu çalıştırın ve Gradle projesini yenileyin.
3.  Eğer kütüphane eksikliği gibi sorunlar yaşarsanız, `gradlew --refresh-dependencies` komutuyla önbelleği yenileyebilir ve `gradlew clean` ile proje dosyalarını temizleyebilirsiniz (kodlarınız etkilenmez). 