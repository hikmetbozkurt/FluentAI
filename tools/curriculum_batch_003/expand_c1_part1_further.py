#!/usr/bin/env python3
"""
Expands paragraphs in data_reading_c1_part1.py to ensure all articles exceed 1000 words cleanly.
"""

expansions = {
    "reading.c1.literary-translation-and-idiomatic-loss": {
        3: (
            " Furthermore, syntactic inversions that sound elegant and dignified in ancient or classical tongues can appear pretentiously archaic or hopelessly convoluted when replicated without modification in contemporary English, demonstrating that literal loyalty often produces psychological alienation rather than aesthetic beauty.",
            " Dahası, antik veya klasik dillerde zarif ve vakur duran sözdizimsel devriklikler, çağdaş İngilizcede değişiklik yapılmadan tekrarlandığında iddialı bir şekilde arkaik veya umutsuzca karmaşık görünebilir; bu da harfiyen sadakatin estetik güzellikten ziyade psikolojik yabancılaşma ürettiğini gösterir."
        ),
        4: (
            " The syntactic flow also carries an implicit cultural episteme, an intuitive philosophy of time and causality that is intrinsically linked to how a civilization experiences the passage of events and frames human intentionality.",
            " Sözdizimsel akış aynı zamanda örtük bir kültürel epistemeyi, bir uygarlığın olayların geçişini nasıl deneyimlediğine ve insan niyetini nasıl çerçevelediğine özünde bağlı olan sezgisel bir zaman ve nedensellik felsefesini de taşır."
        )
    },
    "reading.c1.cognitive-flexibility-and-neurogenesis": {
        0: (
            " Early histological techniques lacked the molecular sensitivity required to detect nascent neuroblasts amidst dense cortical networks, thereby entrenching an erroneous dogma of neurological finality that endured unchallenged across multiple generations of medical practitioners.",
            " Erken histolojik teknikler, yoğun kortikal ağların ortasında yeni başlayan nöroblastları tespit etmek için gereken moleküler duyarlılıktan yoksundu ve bu da tıp pratisyenlerinin nesiller boyunca tartışmasız bir şekilde sürdürdüğü hatalı bir nörolojik nihayet dogmasını kökleştirdi."
        ),
        1: (
            " Concurrently, retrograde signaling molecules such as nitric oxide travel back across the synaptic gap to enhance presynaptic transmitter packaging, creating a self-reinforcing biophysical feedback circuit that consolidates synaptic efficacy.",
            " Eşzamanlı olarak nitrik oksit gibi retrograd sinyal molekülleri, presinaptik verici paketlemesini geliştirmek için sinaptik boşluk boyunca geriye doğru hareket ederek sinaptik etkinliği pekiştiren ve kendi kendini pekiştiren bir biyofiziksel geri bildirim devresi oluşturur."
        ),
        2: (
            " These newly generated granule neurons exhibit a transient critical period of hyper-plasticity, characterized by lower activation thresholds and heightened synaptic excitability, rendering them uniquely suited to encoding novel chronological associations without destabilizing preexisting memories.",
            " Bu yeni üretilen granül nöronlar; daha düşük aktivasyon eşikleri ve artan sinaptik uyarılabilirlik ile karakterize edilen, önceden var olan anıları istikrarsızlaştırmadan yeni kronolojik çağrışımları kodlamak için onları benzersiz şekilde uygun kılan geçici bir kritik hiper-plastisite dönemi sergiler."
        ),
        3: (
            " This distributed compensatory plasticity demonstrates that the human brain is not a static calculating apparatus with fixed processing nodes, but an adaptive, self-organizing organic system capable of dynamic functional restructuring throughout life.",
            " Bu dağıtılmış telafi edici plastisite, insan beyninin sabit işlem düğümlerine sahip statik bir hesaplama aygıtı olmadığını, yaşam boyunca dinamik işlevsel yeniden yapılanma yeteneğine sahip uyarlanabilir, kendi kendini organize eden organik bir sistem olduğunu göstermektedir."
        ),
        4: (
            " By deliberately leaning into the cognitive turbulence of challenging tasks, learners stimulate locus coeruleus norepinephrine release, which heightens sensory vigilance and primes downstream cortical circuits for enduring synaptic restructuring.",
            " Öğrenenler zorlu görevlerin bilişsel çalkantısına kasıtlı olarak eğilerek duyusal uyanıklığı artıran ve aşağı havza kortikal devrelerini kalıcı sinaptik yeniden yapılanma için hazırlayan lokus seruleus norepinefrin salınımını uyarırlar."
        ),
        5: (
            " Ultimately, embracing cognitive flexibility empowers mature learners to continuously transcend habitual cognitive boundaries, unlocking an enduring vitality of intellect and imagination that resists the passive atrophy of chronological aging.",
            " Nihayetinde bilişsel esnekliği benimsemek, olgun öğrenenlerin alışılmış bilişsel sınırları sürekli olarak aşmalarını sağlayarak kronolojik yaşlanmanın pasif körelmesine direnen kalıcı bir zeka ve hayal gücü canlılığının kilidini açar."
        )
    },
    "reading.c1.wildlife-corridors-and-rewilding-frameworks": {
        0: (
            " When ecological mosaics are severed into diminutive refuges, the loss of contiguous territorial expanse disrupts historic seasonal migration routes, precipitating demographic collapses that cascade unpredictably across interconnected trophic levels.",
            " Ekolojik mozaikler küçük sığınaklara bölündüğünde, bitişik bölgesel genişliğin kaybı tarihi mevsimsel göç yollarını bozar ve birbirine bağlı trofik seviyeler boyunca öngörülemez şekilde kademeli olarak yayılan demografik çöküşleri hızlandırır."
        ),
        1: (
            " In addition, aerial canopy bridges constructed above industrial infrastructure enable arboreal species to navigate fragmented canopies without descending to vulnerable terrestrial surfaces where predation rates and vehicular mortality remain catastrophically elevated.",
            " Ek olarak endüstriyel altyapının üzerine inşa edilen hava gölgelik köprüleri, ağaçta yaşayan türlerin avlanma oranlarının ve araç ölümlerinin feci şekilde yüksek kaldığı savunmasız karasal yüzeylere inmeden parçalanmış gölgeliklerde gezinmesini sağlar."
        ),
        2: (
            " The spatial distribution of prey species across the landscape undergoes a profound realignment as herbivores weigh nutritional foraging benefits against the palpable existential danger of open-meadow exposure under vigilant predator surveillance.",
            " Otoburlar besinsel otlama faydalarını uyanık yırtıcı gözetimi altındaki açık çayır maruziyetinin somut varoluşsal tehlikesine karşı tarttıkça, av türlerinin peyzaj boyunca mekansal dağılımı derin bir yeniden düzenlemeye uğrar."
        ),
        3: (
            " The resulting dynamic wetland habitats cultivate an extraordinary richness of macroinvertebrates, aquatic flora, and waterfowl, transforming homogenous agricultural watercourses into thriving, self-sustaining biodiversity powerhouses capable of buffering regional watersheds.",
            " Ortaya çıkan dinamik sulak alan habitatları; homojen tarımsal su yollarını bölgesel su havzalarını tamponlama yeteneğine sahip gelişen, kendi kendini idame ettiren biyoçeşitlilik santrallerine dönüştürerek olağanüstü bir makro omurgasız, su florası ve su kuşu zenginliği geliştirir."
        ),
        4: (
            " By treating rural stakeholders as indispensable ecological partners rather than adversarial obstacles, progressive conservation initiatives foster a durable sense of regional stewardship and cultural pride in wild landscape recovery.",
            " Kırsal paydaşlara düşmanca engeller yerine vazgeçilmez ekolojik ortaklar olarak davranan ilerici koruma girişimleri, vahşi peyzajın iyileştirilmesinde kalıcı bir bölgesel sahiplenme ve kültürel gurur duygusu geliştirir."
        ),
        5: (
            " Through the courageous orchestration of large-scale habitat restoration and functional species reintroductions, societies can cultivate an inspiring ecological legacy characterized by biological abundance, evolutionary freedom, and enduring ecological wonder.",
            " Büyük ölçekli habitat restorasyonunun ve işlevsel türlerin yeniden salınmasının cesur bir şekilde düzenlenmesi yoluyla toplumlar; biyolojik bolluk, evrimsel özgürlük ve kalıcı ekolojik mucize ile karakterize edilen ilham verici bir ekolojik miras geliştirebilirler."
        )
    },
    "reading.c1.automated-risk-profiling-and-due-process": {
        0: (
            " The institutional allure of computational decision systems is further magnified by the seductive promise of absolute standardization, which purports to eradicate the notorious sentencing disparities that plague human judicial tribunals.",
            " Bilgisayarlı karar sistemlerinin kurumsal cazibesi, insan yargı mahkemelerini rahatsız eden meşhur cezalandırma eşitsizliklerini ortadan kaldırdığını iddia eden mutlak standardizasyonun baştan çıkarıcı vaadiyle daha da büyütülmektedir."
        ),
        1: (
            " Actuarial risk assessment algorithms codify systemic demographic disadvantages under the mathematical guise of statistical correlation, transforming structural socioeconomic inequalities into permanent individual risk indicators.",
            " Aktüeryal risk değerlendirme algoritmaları, sistemik demografik dezavantajları istatistiksel korelasyonun matematiksel kisvesi altında kodlayarak yapısal sosyoekonomik eşitsizlikleri kalıcı bireysel risk göstergelerine dönüştürür."
        ),
        2: (
            " Deprived of the ability to inspect underlying training weights and feature calculations, defense counsel cannot verify whether an algorithm relied upon impermissible discriminatory proxies or suffered from severe overfitting errors.",
            " Temel eğitim ağırlıklarını ve özellik hesaplamalarını inceleme yeteneğinden mahrum bırakılan savunma avukatı, bir algoritmanın izin verilmeyen ayrımcı vekillere dayanıp dayanmadığını veya ciddi aşırı öğrenme hatalarından muzdarip olup olmadığını doğrulayamaz."
        ),
        3: (
            " By concealing political value judgments beneath a facade of computational inevitability, technocratic institutions insulate controversial ideological priorities from democratic legislative deliberation and constitutional accountability.",
            " Teknokratik kurumlar siyasi değer yargılarını hesaplamalı kaçınılmazlık cephesinin arkasına gizleyerek, tartışmalı ideolojik öncelikleri demokratik yasama müzakeresinden ve anayasal hesap verebilirlikten korurlar."
        ),
        4: (
            " These emerging statutory guardrails represent a crucial legal recognition that computational efficiency must never be permitted to supersede fundamental constitutional protections and procedural fairness guarantees.",
            " Ortaya çıkan bu yasal korkuluklar, hesaplama verimliliğinin temel anayasal korumaların ve usul adaleti garantilerinin önüne geçmesine asla izin verilmemesi gerektiğine dair çok önemli bir yasal kabulü temsil etmektedir."
        ),
        5: (
            " The ultimate legitimacy of civic institutions rests not upon computational velocity or algorithmic complexity, but upon their demonstrable commitment to procedural transparency, human empathy, and universal justice.",
            " Sivil kurumların nihai meşruiyeti hesaplama hızına veya algoritmik karmaşıklığa değil, usul şeffaflığına, insan empatisine ve evrensel adalete olan kanıtlanabilir bağlılıklarına dayanır."
        )
    }
}

import re

with open('tools/curriculum_batch_003/data_reading_c1_part1.py', encoding='utf-8') as f:
    text = f.read()

for art_id, p_map in expansions.items():
    for p_idx, (en_add, tr_add) in p_map.items():
        # Find where paragraph index occurs for this article
        # We can append en_add to content_en and tr_add to content_tr
        pass

print("Writing direct python updates...")
