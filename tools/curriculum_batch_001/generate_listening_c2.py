#!/usr/bin/env python3
"""
Listening Generator for C2 (11 new scenarios, bringing C2 total to 12).
All scenarios adhere to listening.schema.json:
- CEFR: C2
- Valid category enum
- Real speakers
- Timestamped transcript items with text_en and text_tr
- 5 MCQs per scenario using McqBalancer
- Key vocabulary and related_ids with verified IDs
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=605)

def build_q(qid, q_en, q_tr_hint, correct, distractors, exp_en, exp_tr):
    options, idx = balancer.balance_options(correct, distractors)
    return {
        "id": qid,
        "question_en": q_en,
        "question_tr_hint": q_tr_hint,
        "options": options,
        "correct_answer": correct,
        "explanation_en": exp_en,
        "explanation_tr": exp_tr
    }

C2_LISTENING_SCENARIOS = [
    {
        "id": "listening.c2.anti-fragile-systems-governance",
        "title": "Board Deliberation: Anti-Fragile Governance and Deliberate Redundancy",
        "cefr_level": "C2",
        "category": "executive_briefing",
        "scenario_context": "Kagan, Chief Risk Officer, and Dr. Evelyn Vance, Chief Systems Architect, deliberate anti-fragile systems engineering, operational slack, and chaos engineering in mission-critical banking infrastructure.",
        "speakers": [
            {"id": "kagan", "name": "Kagan", "role": "Chief Risk Officer", "accent": "Turkish"},
            {"id": "evelyn", "name": "Dr. Evelyn Vance", "role": "Chief Systems Architect", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_antifragile_systems.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "kagan",
                "start_ms": 0,
                "end_ms": 7500,
                "text_en": "Evelyn, our management consulting advisors continue to urge aggressive elimination of duplicate cloud clusters to optimize short-term operating margins.",
                "text_tr": "Evelyn, yönetim danışmanı danışmanlarımız kısa vadeli işletme marjlarını optimize etmek için mükerrer bulut kümelerinin agresif bir şekilde ortadan kaldırılmasını telkin etmeye devam ediyor."
            },
            {
                "index": 2,
                "speaker_id": "evelyn",
                "start_ms": 7900,
                "end_ms": 17500,
                "text_en": "That represents a catastrophic conflation of hyper-efficiency with institutional strength, Kagan. Nassim Taleb's anti-fragility framework demonstrates that ruthless elimination of redundancy guarantees systemic fragility under unexpected tail-risk events.",
                "text_tr": "Bu aşırı verimliliği kurumsal güçle vahim biçimde karıştırmaktır Kagan. Nassim Taleb'in kırılganlık karşıtlığı çerçevesi, yedekliliğin acımasızca yok edilmesinin beklenmedik kuyruk riski olaylarında sistemik kırılganlığı garanti ettiğini kanıtlar."
            },
            {
                "index": 3,
                "speaker_id": "kagan",
                "start_ms": 17900,
                "end_ms": 26000,
                "text_en": "How do you substantiate the commercial necessity of preserving deliberate operational slack when presenting our infrastructure capital expenditures to the board audit committee?",
                "text_tr": "Yönetim kurulu denetim komitesine altyapı sermaye harcamalarımızı sunarken kasti operasyonel esneklik payı bırakmanın ticari gerekliliğini nasıl somutlaştırıyorsun?"
            },
            {
                "index": 4,
                "speaker_id": "evelyn",
                "start_ms": 26400,
                "end_ms": 35800,
                "text_en": "Through empirical telemetry derived from continuous chaos engineering experiments. By deliberately injecting adversarial network partitions and power loss simulations, we demonstrate that systems with ten percent operational buffer absorb shocks without customer disruption.",
                "text_tr": "Sürekli kaos mühendisliği deneylerinden elde edilen ampirik telemetri yoluyla. Kasti olarak düşmanca ağ bölünmeleri ve güç kaybı simülasyonları enjekte ederek, yüzde onluk operasyonel tampona sahip sistemlerin müşteri kesintisi olmadan şokları emdiğini kanıtlıyoruz."
            },
            {
                "index": 5,
                "speaker_id": "kagan",
                "start_ms": 36200,
                "end_ms": 45500,
                "text_en": "So rather than merely withstanding shocks in a passive robust state, an anti-fragile architecture actively improves its evolutionary heuristics when subjected to controlled volatility?",
                "text_tr": "Yani pasif ve sağlam bir durumda yalnızca şoklara dayanmak yerine, kırılganlık karşıtı bir mimari kontrollü oynaklığa maruz kaldığında evrimsel sezgilerini aktif olarak geliştiriyor mu?"
            },
            {
                "index": 6,
                "speaker_id": "evelyn",
                "start_ms": 45900,
                "end_ms": 55000,
                "text_en": "Precisely. Every localized failure surfaces an unmonitored invariant, enabling autonomous self-healing topologies. We must treat deliberate redundancy not as an idle cost center, but as an indispensable existential insurance policy.",
                "text_tr": "Kesinlikle. Her yerel arıza izlenmeyen bir değişmezi su yüzüne çıkararak otonom kendi kendini iyileştiren topolojilere olanak tanır. Kasti yedekliliği atıl bir maliyet merkezi olarak değil, vazgeçilmez bir varoluşsal sigorta poliçesi olarak ele almalıyız."
            }
        ],
        "key_vocabulary": [
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Kaos mühendisliği ve telemetri verileriyle operasyonel gereksinimleri somutlaştırmak."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Kriz anlarında ayakta kalma ve kırılganlık karşıtı sistemik toparlanma."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_ant_01",
                "What management consulting recommendation does Dr. Evelyn Vance vigorously repudiate?",
                "Dr. Evelyn Vance yönetim danışmanlarının hangi tavsiyesini şiddetle reddetmektedir?",
                "The aggressive elimination of duplicate cloud clusters to maximize short-term operating margins",
                [
                    "The proposal to hire additional software testing interns during the summer",
                    "The mandate to migrate corporate email servers to modern cloud providers",
                    "The installation of solar panels on the roof of the corporate office"
                ],
                "Evelyn warns that aggressively eliminating duplicate clusters confuses hyper-efficiency with resilience.",
                "Evelyn kümelerin kaldırılmasının aşırı verimliliği dayanıklılıkla karıştırmak olduğunu söyleyerek karşı çıkar."
            ),
            build_q(
                "q_c2_ant_02",
                "According to Nassim Taleb's anti-fragility framework cited by Evelyn, what does ruthless efficiency cause?",
                "Evelyn tarafından atıfta bulunulan Nassim Taleb'in anti-kırılganlık çerçevesine göre acımasız verimlilik neye yol açar?",
                "It guarantees acute systemic fragility under unexpected tail-risk disruptions",
                [
                    "It ensures infinite corporate profitability without any risk of bankruptcy",
                    "It eliminates the necessity for human software engineers entirely",
                    "It reduces electrical power consumption to zero across all servers"
                ],
                "Evelyn explains that ruthless elimination of redundancy guarantees fragility under tail risks.",
                "Evelyn yedekliliği yok etmenin kuyruk riskleri karşısında kırılganlığı kaçınılmaz kıldığını vurgular."
            ),
            build_q(
                "q_c2_ant_03",
                "How does Evelyn substantiate the commercial need for operational slack to the board?",
                "Evelyn operasyonel esneklik payının ticari gerekliliğini yönetim kuruluna nasıl somutlaştırmaktadır?",
                "Through empirical telemetry generated by continuous chaos engineering experiments and simulated partitions",
                [
                    "By quoting ancient classical poetry written in classical Latin",
                    "By threatening to resign from the organization immediately",
                    "By organizing an unannounced general strike among junior developers"
                ],
                "Evelyn relies on empirical telemetry from chaos engineering experiments injecting partitions and power loss.",
                "Evelyn ağ bölünmeleri ve güç kaybı simüle eden kaos mühendisliği telemetrisine dayanır."
            ),
            build_q(
                "q_c2_ant_04",
                "What distinguishes an 'anti-fragile' architecture from a merely 'robust' system?",
                "'Anti-kırılgan' bir mimariyi yalnızca 'sağlam' bir sistemden ayıran temel fark nedir?",
                "An anti-fragile system actively learns and improves its heuristics when exposed to controlled volatility",
                [
                    "An anti-fragile system is completely made of physical stone rather than computers",
                    "An anti-fragile system never runs any software programs or processes",
                    "An anti-fragile system costs zero dollars to maintain over fifty years"
                ],
                "Kagan and Evelyn agree that anti-fragile systems actively improve when exposed to controlled volatility.",
                "Anti-kırılgan sistemlerin kontrollü oynaklığa maruz kaldığında aktif olarak kendini geliştirdiği belirtilir."
            ),
            build_q(
                "q_c2_ant_05",
                "How does Evelyn characterize deliberate architectural redundancy at the conclusion?",
                "Evelyn sonuç bölümünde kasti mimari yedekliliği nasıl nitelendirmektedir?",
                "As an indispensable existential insurance policy rather than an idle cost center",
                [
                    "As an embarrassing waste of company shareholder capital",
                    "As a temporary hack that will be deleted next quarter",
                    "As an optional luxury reserved only for technology giants"
                ],
                "Evelyn insists on treating redundancy as an indispensable existential insurance policy.",
                "Evelyn yedekliliğin vazgeçilmez bir varoluşsal sigorta poliçesi olarak görülmesi gerektiğini ifade eder."
            )
        ],
        "topic_tags": ["anti-fragility", "chaos-engineering", "systems-architecture", "risk-governance", "c2-mastery"],
        "related_ids": ["vocab.substantiate", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.quantum-cryptography-transition",
        "title": "Cryptographic Engineering: Post-Quantum Migration and Lattice Schemes",
        "cefr_level": "C2",
        "category": "engineering_meeting",
        "scenario_context": "Taner, principal cryptographic engineer, and Prof. Alistair Finch, head of quantum security, evaluate migrating enterprise banking HSMs from RSA and ECDSA to NIST post-quantum lattice algorithms.",
        "speakers": [
            {"id": "taner", "name": "Taner", "role": "Principal Cryptographic Engineer", "accent": "Turkish"},
            {"id": "finch", "name": "Prof. Alistair Finch", "role": "Head of Quantum Security", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_post_quantum.mp3",
        "duration_seconds": 57,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "finch",
                "start_ms": 0,
                "end_ms": 7800,
                "text_en": "Taner, with recent advances in fault-tolerant quantum error correction, Shor's algorithm poses an imminent existential threat to all public-key infrastructure based on integer factorization and discrete logarithms.",
                "text_tr": "Taner, hataya dayanıklı kuantum hata düzeltmesindeki son gelişmelerle birlikte Shor algoritması tamsayı çarpanlara ayırma ve ayrık logaritmaya dayalı tüm açık anahtar altyapısına doğrudan varoluşsal bir tehdit oluşturuyor."
            },
            {
                "index": 2,
                "speaker_id": "taner",
                "start_ms": 8200,
                "end_ms": 17800,
                "text_en": "Indeed Professor Finch. Adversarial state actors are already executing 'harvest-now, decrypt-later' campaigns, intercepting and storing encrypted banking telemetry to decrypt retrospectively once cryptanalytically relevant quantum computers materialize.",
                "text_tr": "Kesinlikle Profesör Finch. Düşman devlet aktörleri kriptanalitik açıdan yetkin kuantum bilgisayarlar ortaya çıktığında geriye dönük deşifre etmek üzere şifreli bankacılık telemetrisini şimdiden toplayıp saklayan 'şimdi topla, sonra çöz' operasyonları yürütüyor."
            },
            {
                "index": 3,
                "speaker_id": "finch",
                "start_ms": 18200,
                "end_ms": 26800,
                "text_en": "NIST has formally standardized post-quantum schemes: ML-KEM for key encapsulation and ML-DSA for digital signatures. What performance trade-offs do these lattice constructions impose upon our high-frequency payment gateways?",
                "text_tr": "NIST kuantum sonrası şemaları resmen standartlaştırdı: Anahtar kapsülleme için ML-KEM ve dijital imzalar için ML-DSA. Bu kafes (lattice) yapıları yüksek frekanslı ödeme ağ geçitlerimize hangi performans ödünleşimlerini dayatıyor?"
            },
            {
                "index": 4,
                "speaker_id": "taner",
                "start_ms": 27200,
                "end_ms": 36800,
                "text_en": "Public key and signature payload sizes balloon by orders of magnitude: an ML-KEM-768 encapsulation payload is nearly twelve hundred bytes compared to thirty-two bytes for Curve25519, threatening TCP packet fragmentation.",
                "text_tr": "Açık anahtar ve imza veri yükü boyutları kat kat büyüyor: Bir ML-KEM-768 kapsülleme yükü Curve25519'daki otuz iki bayta kıyasla yaklaşık bin iki yüz bayttır ve bu da TCP paket parçalanması tehdidi yaratır."
            },
            {
                "index": 5,
                "speaker_id": "finch",
                "start_ms": 37200,
                "end_ms": 46000,
                "text_en": "How do you propose safeguarding transaction throughput while mitigating the unverified mathematical risks of nascent lattice-based assumptions?",
                "text_tr": "Henüz yeni olan kafes tabanlı varsayımların doğrulanmamış matematiksel risklerini azaltırken işlem verimini nasıl korumayı öneriyorsun?"
            },
            {
                "index": 6,
                "speaker_id": "taner",
                "start_ms": 46400,
                "end_ms": 56000,
                "text_en": "We will implement hybrid post-quantum key exchange: combining classical X25519 ECDH with ML-KEM in a dual-encapsulation handshake. Compromising the session requires breaking both mathematical paradigms simultaneously.",
                "text_tr": "Hibrit kuantum sonrası anahtar değişimi uygulayacağız: Klasik X25519 ECDH ile ML-KEM'i çift kapsüllemeli bir el sıkışmada birleştireceğiz. Oturumu ele geçirmek her iki matematiksel paradigmayı aynı anda kırmayı gerektirir."
            }
        ],
        "key_vocabulary": [
            {
                "word": "entropy",
                "vocab_id": "vocab.entropy",
                "context_note_tr": "Kuantum sonrası kriptografide anahtar türetimi ve rastgelelik entropisi."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Kuantum bilgisayarların Shor algoritması saldırılarına karşı kriptografik dayanıklılık."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_qnt_01",
                "Why is the 'harvest-now, decrypt-later' espionage campaign considered an urgent threat today?",
                "'Şimdi topla, sonra çöz' casusluk faaliyeti neden bugün acil bir tehdit olarak görülmektedir?",
                "Adversaries are intercepting and storing encrypted data today to decrypt once quantum computers become available",
                [
                    "Adversaries are physically stealing paper cash from bank automated teller machines",
                    "Quantum computers have already decoded every password in the world yesterday",
                    "Computer monitors turn off automatically when exposed to quantum radio waves"
                ],
                "Taner explains that state actors are intercepting encrypted traffic now to decrypt it once quantum hardware arrives.",
                "Taner aktörlerin şifreli verileri ileride kuantum bilgisayarlarla çözmek üzere şimdiden topladığını belirtir."
            ),
            build_q(
                "q_c2_qnt_02",
                "Which two post-quantum algorithmic schemes were formally standardized by NIST?",
                "NIST tarafından resmen hangi iki kuantum sonrası algoritma şeması standartlaştırıldı?",
                "ML-KEM for key encapsulation and ML-DSA for digital signatures",
                [
                    "RSA-1024 and MD5 hashing algorithms",
                    "Bitcoin Proof of Work and Ethereum Smart Contracts",
                    "Rot13 cipher and Morse code transmission"
                ],
                "Finch cites ML-KEM for key encapsulation and ML-DSA for digital signatures.",
                "Finch ML-KEM ve ML-DSA şemalarını açıklar."
            ),
            build_q(
                "q_c2_qnt_03",
                "What network protocol complication is introduced by lattice-based public key sizes?",
                "Kafes tabanlı açık anahtar boyutları hangi ağ protokolü komplikasyonunu beraberinde getirmektedir?",
                "Payloads expand to nearly twelve hundred bytes, threatening TCP packet fragmentation and handshake latency",
                [
                    "Network cables physically overheat and melt during transmission",
                    "Internet service providers charge ten thousand dollars per packet",
                    "Computer keyboards lose their ability to type capital letters"
                ],
                "Taner explains ML-KEM-768 payloads reach 1,200 bytes compared to 32 bytes, risking TCP fragmentation.",
                "Taner 1200 bayta çıkan yüklerin TCP paket parçalanması riski yarattığını açıklar."
            ),
            build_q(
                "q_c2_qnt_04",
                "What security design does Taner propose to hedge against unproven mathematical assumptions in lattice cryptography?",
                "Taner kafes kriptografisindeki kanıtlanmamış varsayımlara karşı hangi güvenlik tasarımını önermektedir?",
                "A hybrid key exchange combining classical X25519 ECDH with ML-KEM in dual encapsulation",
                [
                    "Abandoning all computer encryption and using paper couriers",
                    "Relying exclusively on forty-character passwords updated daily",
                    "Encrypting data twice using the exact same RSA key"
                ],
                "Taner proposes a hybrid post-quantum key exchange combining X25519 ECDH with ML-KEM.",
                "Taner klasik X25519 ile ML-KEM'i birleştiren hibrit bir yaklaşım önerir."
            ),
            build_q(
                "q_c2_qnt_05",
                "What must an adversary accomplish to break the proposed hybrid post-quantum handshake?",
                "Saldırganın önerilen hibrit kuantum sonrası el sıkışmayı kırması için neyi başarması gerekir?",
                "Break both classical elliptic-curve and post-quantum lattice mathematical paradigms simultaneously",
                [
                    "Guess a single four-digit numeric PIN code",
                    "Find the physical building where the servers are located",
                    "Wait for thirty seconds after the handshake concludes"
                ],
                "Taner highlights that breaking the session requires breaking both mathematical paradigms simultaneously.",
                "Taner her iki matematiksel paradigmanın aynı anda kırılmasını gerektirdiğini vurgular."
            )
        ],
        "topic_tags": ["cryptography", "quantum-computing", "post-quantum-cryptography", "security-architecture", "c2-mastery"],
        "related_ids": ["vocab.entropy", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.geopolitical-supply-chain-decoupling",
        "title": "Geopolitical Strategy: Semiconductor Fab Concentration and Decoupling",
        "cefr_level": "C2",
        "category": "executive_briefing",
        "scenario_context": "Sibel, Chief Supply Chain Officer, and Marcus Vance, Board Vice Chairman, analyze the geopolitical concentration of advanced node semiconductor fabrication and contingency decoupling.",
        "speakers": [
            {"id": "sibel", "name": "Sibel", "role": "Chief Supply Chain Officer", "accent": "Turkish"},
            {"id": "marcus", "name": "Marcus Vance", "role": "Board Vice Chairman", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c2_semiconductor_decoupling.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "marcus",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Sibel, the board audit committee is gravely alarmed by our tier-one hardware exposure. Over ninety percent of our proprietary AI accelerator silicon is fabricated within a single island maritime corridor.",
                "text_tr": "Sibel, yönetim kurulu denetim komitesi birinci kademe donanım risk maruziyetimizden ciddi şekilde endişeli. Özel yapay zeka hızlandırıcı silikonumuzun yüzde doksanından fazlası tek bir ada deniz koridorunda üretiliyor."
            },
            {
                "index": 2,
                "speaker_id": "sibel",
                "start_ms": 7600,
                "end_ms": 17200,
                "text_en": "The strategic bottleneck is acute, Marcus. Sub-three-nanometer extreme ultraviolet lithography remains monopolized by TSMC in Taiwan, dependent entirely on ASML optical mirrors from the Netherlands and specialized photoresists from Japan.",
                "text_tr": "Stratejik darboğaz çok ciddi Marcus. Üç nanometre altı aşırı ultraviyole (EUV) litografi, Tayvan'daki TSMC tekelinde kalmaya devam ediyor ve tamamen Hollanda'dan gelen ASML optik aynalarına ve Japonya'dan gelen özel fotorezistlere bağımlı."
            },
            {
                "index": 3,
                "speaker_id": "marcus",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "If a regional naval blockade or seismic event suspends shipping for even ninety days, our cloud infrastructure expansion grinds to an absolute halt globally.",
                "text_tr": "Bölgesel bir deniz ablukası veya sismik olay sevkiyatı doksan gün bile durdursa küresel çapta bulut altyapı genişlememiz tamamen durma noktasına gelir."
            },
            {
                "index": 4,
                "speaker_id": "sibel",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "Precisely. Therefore, I propose diversifying our foundry allocation by qualifying Intel Foundry Services in Ohio and TSMC's nascent fabrication facilities in Arizona and Dresden.",
                "text_tr": "Kesinlikle. Bu nedenle Ohio'daki Intel Dökümhane Hizmetleri'ni ve TSMC'nin Arizona ile Dresden'deki yeni dökümhane tesislerini yetkilendirerek dökümhane tahsisimizi çeşitlendirmeyi öneriyorum."
            },
            {
                "index": 5,
                "speaker_id": "marcus",
                "start_ms": 35900,
                "end_ms": 44800,
                "text_en": "What unit cost penalty and yield degradation must we absorb during the initial tape-out and secondary fab qualification cycles?",
                "text_tr": "İlk entegre devre tasarımı (tape-out) ve ikincil dökümhane yetkilendirme döngüleri sırasında hangi birim maliyet cezasını ve verim kaybını kabullenmek zorundayız?"
            },
            {
                "index": 6,
                "speaker_id": "sibel",
                "start_ms": 45200,
                "end_ms": 54500,
                "text_en": "Silicon wafer yields in Arizona will initially trail Taiwan by approximately fifteen percent, increasing unit production costs by twenty-two percent. However, that premium buys sovereign supply chain continuity.",
                "text_tr": "Arizona'daki silikon gofret verimi başlangıçta Tayvan'ın yaklaşık yüzde on beş gerisinde kalacak ve birim üretim maliyetlerini yüzde yirmi iki artıracaktır. Ancak bu prim, egemen tedarik zinciri sürekliliğini satın alır."
            }
        ],
        "key_vocabulary": [
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Tedarik zinciri risk analizlerini jeopolitik ampirik verilerle somutlaştırmak."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Yarı iletken tedarikinde çok kıtalı dökümhane dayanıklılığı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_semi_01",
                "What geographic concentration risk threatens the company's proprietary AI accelerator silicon?",
                "Şirketin özel yapay zeka hızlandırıcı silikonunu hangi coğrafi yoğunlaşma riski tehdit etmektedir?",
                "Over ninety percent of fabrication is concentrated within a single island maritime corridor",
                [
                    "All microchips are manufactured manually inside local university classrooms",
                    "Computer microchips are transported exclusively by passenger trains across Antarctica",
                    "Semiconductor patents are held entirely by private commercial banks"
                ],
                "Marcus opens by noting over 90% of proprietary silicon is fabricated in a single island maritime corridor.",
                "Marcus silikon üretiminin %90'ından fazlasının tek bir ada deniz koridorunda toplandığını belirtir."
            ),
            build_q(
                "q_c2_semi_02",
                "Which technology company holds an effective monopoly over sub-three-nanometer EUV lithography?",
                "Üç nanometre altı EUV litografisinde hangi teknoloji şirketi fiili bir tekele sahiptir?",
                "TSMC in Taiwan, dependent on ASML optics from the Netherlands",
                [
                    "A private bicycle manufacturing factory in Switzerland",
                    "A local software startup based in London",
                    "A state-owned timber logging company in Canada"
                ],
                "Sibel explains sub-3nm lithography is monopolized by TSMC, dependent on ASML and Japanese chemicals.",
                "Sibel litografinin Hollandalı ASML ve TSMC tekelinde olduğunu açıklar."
            ),
            build_q(
                "q_c2_semi_03",
                "What global consequence would result if shipping from that region were halted for 90 days?",
                "O bölgeden sevkiyatlar 90 gün durursa küresel ölçekte nasıl bir sonuç doğar?",
                "Global cloud infrastructure expansion would grind to an absolute halt",
                [
                    "Computer programmers would be legally required to work outdoors",
                    "All commercial airplanes would cease flying immediately",
                    "The price of paper banknotes would increase by one thousand percent"
                ],
                "Marcus warns that a 90-day blockade would bring cloud infrastructure expansion to an absolute halt globally.",
                "Marcus 90 günlük ablukanın bulut altyapı genişlemesini tamamen durduracağını belirtir."
            ),
            build_q(
                "q_c2_semi_04",
                "Which secondary semiconductor fabrication facilities does Sibel propose qualifying?",
                "Sibel hangi ikincil yarı iletken dökümhane tesislerini yetkilendirmeyi önermektedir?",
                "Intel Foundry Services in Ohio and TSMC facilities in Arizona and Dresden",
                [
                    "Underground copper mines in South America",
                    "Commercial warehouse facilities in Iceland",
                    "Local electronics repair shops in Paris"
                ],
                "Sibel proposes qualifying Intel Foundry Services in Ohio and TSMC plants in Arizona and Dresden.",
                "Sibel Ohio, Arizona ve Dresden tesislerini yetkilendirmeyi önerir."
            ),
            build_q(
                "q_c2_semi_05",
                "What production cost increase does Sibel anticipate during the initial transition?",
                "Sibel ilk geçiş döneminde hangi üretim maliyeti artışını öngörmektedir?",
                "A twenty-two percent increase in unit production costs",
                [
                    "A ninety-nine percent cost reduction",
                    "Zero financial difference in wafer production",
                    "A five hundred percent price surge"
                ],
                "Sibel confirms unit production costs will increase by 22% due to initial yield trailing by 15%.",
                "Sibel verim düşüklüğü nedeniyle birim maliyetin %22 artacağını belirtir."
            )
        ],
        "topic_tags": ["semiconductors", "geopolitics", "supply-chain", "resilience", "c2-mastery"],
        "related_ids": ["vocab.substantiate", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.sovereign-ai-infrastructure-procurement",
        "title": "Energy & Supercomputing: Nuclear Power Purchase Agreements for AI Datacenters",
        "cefr_level": "C2",
        "category": "negotiation",
        "scenario_context": "Emre, VP of Datacenter Infrastructure, negotiates a multi-gigawatt nuclear power purchase agreement with Lord Henry Sterling, strategic energy director, to power sovereign AI clusters.",
        "speakers": [
            {"id": "emre", "name": "Emre", "role": "VP of Datacenter Infrastructure", "accent": "Turkish"},
            {"id": "sterling", "name": "Lord Henry Sterling", "role": "Strategic Energy Director", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_nuclear_ai.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "emre",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Lord Sterling, our planned sovereign artificial intelligence supercluster requires two gigawatts of continuous, non-intermittent baseload electricity by 2028. Intermittent solar and wind are inadequate.",
                "text_tr": "Lord Sterling, planlanan egemen yapay zeka süper kümemiz 2028 yılına kadar iki gigavatlık kesintisiz, kesintiye uğramayan temel yük elektriği gerektiriyor. Kesintili güneş ve rüzgar enerjisi yetersizdir."
            },
            {
                "index": 2,
                "speaker_id": "sterling",
                "start_ms": 7600,
                "end_ms": 16800,
                "text_en": "Understood, Emre. The public regional electrical grid cannot accommodate such extraordinary concentrated thermal load without triggering severe civilian brownouts across the metropolitan area.",
                "text_tr": "Anlaşıldı Emre. Kamu bölgesel elektrik şebekesi metropol genelinde sivil elektrik kesintilerini tetiklemeden böylesine olağanüstü yoğunlaşmış bir termal yükü kaldıramaz."
            },
            {
                "index": 3,
                "speaker_id": "emre",
                "start_ms": 17200,
                "end_ms": 25800,
                "text_en": "Which is why we are proposing a direct twenty-year Power Purchase Agreement co-located behind the meter at your Sizewell nuclear generation facility.",
                "text_tr": "İşte tam da bu nedenle Sizewell nükleer üretim tesisinizde sayaç arkasında doğrudan yirmi yıllık bir Enerji Satın Alma Sözleşmesi (PPA) öneriyoruz."
            },
            {
                "index": 4,
                "speaker_id": "sterling",
                "start_ms": 26200,
                "end_ms": 35200,
                "text_en": "Co-locating a hyperscale datacenter behind the reactor meter bypasses grid interconnection queue latency. However, what are you willing to guarantee regarding capital underwriting for our reactor lifetime extension?",
                "text_tr": "Reaktör sayacının arkasında bir süper ölçekli veri merkezi kurmak şebeke bağlantı kuyruğu gecikmesini baypas eder. Ancak reaktör ömrü uzatmamızın sermaye garantisi konusunda ne taahhüt etmeye hazırsınız?"
            },
            {
                "index": 5,
                "speaker_id": "emre",
                "start_ms": 35600,
                "end_ms": 44500,
                "text_en": "We will provide eight hundred million dollars in upfront capital expenditure financing and guarantee a ninety-dollar-per-megawatt-hour floor price across the entire twenty-year duration.",
                "text_tr": "Sekiz yüz milyon dolarlık peşin sermaye harcaması finansmanı sağlayacak ve yirmi yıllık süre boyunca megavat-saat başına doksan dolarlık bir taban fiyat garantisi vereceğiz."
            },
            {
                "index": 6,
                "speaker_id": "sterling",
                "start_ms": 44900,
                "end_ms": 54200,
                "text_en": "That financial floor de-risks our nuclear regulatory relicensing. I will submit the draft commercial PPA term sheet to our executive board for definitive ratification.",
                "text_tr": "Bu finansal taban, nükleer düzenleyici yeniden lisanslama sürecimizin riskini azaltır. Kesin onay için taslak ticari PPA şartname belgesini icra kurulumuza sunacağım."
            }
        ],
        "key_vocabulary": [
            {
                "word": "consensus",
                "vocab_id": "vocab.consensus",
                "context_note_tr": "Yüksek riskli nükleer enerji ve süper bilgisayar altyapı mutabakatı."
            },
            {
                "word": "pragmatic",
                "vocab_id": "vocab.pragmatic",
                "context_note_tr": "Kamu şebekesi tıkanıklığını aşmak için sayaç arkası nükleer enerji çözümü."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_nuc_01",
                "What power requirement is demanded for the planned sovereign AI supercluster by 2028?",
                "2028 yılına kadar planlanan egemen yapay zeka süper kümesi için hangi güç gereksinimi talep edilmektedir?",
                "Two gigawatts of continuous, non-intermittent baseload electricity",
                [
                    "Five hundred watts powered by consumer household batteries",
                    "Ten megawatts generated exclusively by pedal bicycles",
                    "Zero electrical power by relying on daylight mirrors"
                ],
                "Emre opens stating the supercluster requires 2 gigawatts of continuous baseload electricity by 2028.",
                "Emre 2028'e kadar 2 gigavatlık kesintisiz temel yük elektriği gerektiğini belirtir."
            ),
            build_q(
                "q_c2_nuc_02",
                "Why is the public regional electrical grid incapable of supporting this facility directly?",
                "Kamu bölgesel elektrik şebekesi bu tesisi neden doğrudan destekleyememektedir?",
                "The concentrated thermal load would trigger severe civilian brownouts across the metropolitan area",
                [
                    "Commercial laws forbid companies from connecting computers to electrical sockets",
                    "The electrical grid operates on alternating current while computers require water",
                    "Government ministers refused to build roads near the datacenter"
                ],
                "Sterling explains the grid cannot support such load without triggering civilian brownouts.",
                "Sterling şebekenin sivil kesintilere yol açmadan bu yükü kaldıramayacağını söyler."
            ),
            build_q(
                "q_c2_nuc_03",
                "What structural co-location strategy does Emre propose to bypass grid connection queues?",
                "Emre şebeke bağlantı kuyruklarını baypas etmek için hangi ortak yerleşim stratejisini önermektedir?",
                "Co-locating the datacenter directly behind the meter at the Sizewell nuclear facility",
                [
                    "Building the datacenter on a floating cargo barge in international ocean waters",
                    "Burying the datacenter four miles beneath the Earth's crust",
                    "Operating the servers exclusively during nighttime hours"
                ],
                "Emre proposes a 20-year PPA co-located behind the meter at the Sizewell nuclear generation facility.",
                "Emre Sizewell nükleer tesisinde sayaç arkasında yerleşmeyi önerir."
            ),
            build_q(
                "q_c2_nuc_04",
                "What upfront capital financing does Emre's organization commit to providing?",
                "Emre'nin kurumu hangi peşin sermaye finansmanını taahhüt etmektedir?",
                "Eight hundred million dollars in upfront financing",
                [
                    "Ten thousand dollars",
                    "Fifty billion dollars",
                    "Two hundred million British pounds in cash banknotes"
                ],
                "Emre confirms providing $800 million in upfront CapEx financing.",
                "Emre peşin 800 milyon dolarlık CapEx finansmanı sağlayacaklarını açıklar."
            ),
            build_q(
                "q_c2_nuc_05",
                "What electricity price floor is guaranteed across the twenty-year Power Purchase Agreement?",
                "Yirmi yıllık Enerji Satın Alma Sözleşmesi boyunca hangi elektrik taban fiyatı garanti edilmektedir?",
                "Ninety dollars per megawatt-hour",
                [
                    "One dollar per megawatt-hour",
                    "Five hundred dollars per kilowatt-hour",
                    "Free electricity for twenty years"
                ],
                "Emre guarantees a $90 per megawatt-hour floor price across 20 years.",
                "Emre 20 yıl boyunca MWh başına 90 dolarlık taban fiyat garantisi verir."
            )
        ],
        "topic_tags": ["energy-infrastructure", "nuclear-power", "sovereign-ai", "supercomputing", "c2-mastery"],
        "related_ids": ["vocab.consensus", "vocab.pragmatic"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.hostile-takeover-defensive-recapitulation",
        "title": "Corporate Finance: Hostile Takeover Defense and Poison Pill Tactics",
        "cefr_level": "C2",
        "category": "negotiation",
        "scenario_context": "Melda, Chief Financial Officer, and Sir Julian Croft, senior M&A legal advisor, formulate defensive poison pill mechanisms against an activist hedge fund's hostile tender offer.",
        "speakers": [
            {"id": "melda", "name": "Melda", "role": "Chief Financial Officer", "accent": "Turkish"},
            {"id": "croft", "name": "Sir Julian Croft", "role": "Senior M&A Defense Advisor", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_hostile_takeover.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "croft",
                "start_ms": 0,
                "end_ms": 7500,
                "text_en": "Melda, activist fund Horizon Capital has accumulated a nine point nine percent stake through synthetic total return swaps, evading Hart-Scott-Rodino early antitrust disclosure thresholds.",
                "text_tr": "Melda, aktivist fon Horizon Capital sentetik toplam getiri takasları (swap) yoluyla Hart-Scott-Rodino erken antitröst bildirim eşiklerini atlatarak yüzde dokuz virgül dokuzluk bir hisse biriktirdi."
            },
            {
                "index": 2,
                "speaker_id": "melda",
                "start_ms": 7900,
                "end_ms": 17200,
                "text_en": "Their public letter demands liquidating our enterprise R&D division and distributing a four-billion-dollar leveraged dividend. It is predatory short-term financial engineering that would gut our technological moat.",
                "text_tr": "Aleni mektupları kurumsal Ar-Ge bölümümüzü tasfiye etmemizi ve dört milyar dolarlık kaldıraçlı temettü dağıtmamızı talep ediyor. Bu teknolojik hendeğimizi yok edecek yağmacı, kısa vadeli bir finans mühendisliğidir."
            },
            {
                "index": 3,
                "speaker_id": "croft",
                "start_ms": 17600,
                "end_ms": 26500,
                "text_en": "The board must immediately adopt a shareholder rights plan—a 'flip-in' poison pill triggered if any entity acquires beneficial ownership exceeding twelve percent.",
                "text_tr": "Yönetim kurulu derhal bir hissedar hakları planını—herhangi bir varlığın yüzde on ikiyi aşan intifa hakkı edinmesi durumunda tetiklenen bir 'flip-in' zehir hapını—kabul etmelidir."
            },
            {
                "index": 4,
                "speaker_id": "melda",
                "start_ms": 26900,
                "end_ms": 36200,
                "text_en": "Under Delaware corporate law, can the poison pill withstand legal challenge under the Unocal and Revlon fiduciary scrutiny standards?",
                "text_tr": "Delaware şirketler hukuku kapsamında zehir hapı Unocal ve Revlon mütevelli denetim standartları altındaki yasal itirazlara dayanabilir mi?"
            },
            {
                "index": 5,
                "speaker_id": "croft",
                "start_ms": 36600,
                "end_ms": 45800,
                "text_en": "Yes, provided the defensive mechanism is proportional to the demonstrated threat to corporate policy and effectiveness, rather than an entrenchment mechanism for incumbent directors.",
                "text_tr": "Evet, savunma mekanizmasının mevcut yöneticileri koltuklarında tutma mekanizması olmaktan ziyade kurumsal politika ve etkinliğe yönelik kanıtlanmış tehditle orantılı olması koşuluyla dayanır."
            },
            {
                "index": 6,
                "speaker_id": "melda",
                "start_ms": 46200,
                "end_ms": 55500,
                "text_en": "Concurrently, we will invite a friendly 'white knight' sovereign wealth fund to acquire a fifteen percent convertible preferred equity tranche, insulating our long-term strategic roadmap.",
                "text_tr": "Eşzamanlı olarak uzun vadeli stratejik yol haritamızı korumak için dostane bir 'beyaz şövalye' varlık fonunu yüzde on beşlik dönüştürülebilir imtiyazlı hisse dilimini almaya davet edeceğiz."
            }
        ],
        "key_vocabulary": [
            {
                "word": "adjudicate",
                "vocab_id": "vocab.adjudicate",
                "context_note_tr": "Delaware kurumsal ticaret mahkemelerinde mütevelli sorumlulukların hükme bağlanması."
            },
            {
                "word": "consensus",
                "vocab_id": "vocab.consensus",
                "context_note_tr": "Yönetim kurulu üyeleri arasında savunma stratejisi uzlaşısı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_take_01",
                "How did Horizon Capital secretly accumulate a 9.9% stake without early regulatory disclosure?",
                "Horizon Capital erken düzenleyici bildirimde bulunmadan nasıl gizlice %9,9'luk hisse biriktirdi?",
                "Through synthetic total return swaps that evaded Hart-Scott-Rodino filing thresholds",
                [
                    "By printing fake paper stock certificates in an underground basement",
                    "By hacking into the company's private computer servers at night",
                    "By bribing junior receptionists at corporate headquarters"
                ],
                "Croft explains Horizon Capital used synthetic total return swaps to evade disclosure thresholds.",
                "Croft sentetik getiri swap'ları kullanarak bildirim eşiklerini atlattıklarını açıklar."
            ),
            build_q(
                "q_c2_take_02",
                "What predatory restructuring does the activist fund's public letter demand?",
                "Aktivist fonun aleni mektubu hangi yağmacı yeniden yapılanmayı talep etmektedir?",
                "Liquidating the enterprise R&D division and distributing a four-billion-dollar leveraged dividend",
                [
                    "Replacing all human employees with domestic farm animals",
                    "Moving corporate headquarters to the middle of the Sahara Desert",
                    "Giving away all software products completely free of charge"
                ],
                "Melda points out they demand liquidating R&D and distributing a $4 billion leveraged dividend.",
                "Melda Ar-Ge'nin tasfiyesini ve 4 milyar dolarlık borçlu temettü dağıtımını istediklerini söyler."
            ),
            build_q(
                "q_c2_take_03",
                "At what ownership percentage will the proposed 'flip-in' poison pill trigger?",
                "Önerilen 'flip-in' zehir hapı yüzde kaçlık mülkiyet oranında tetiklenecektir?",
                "If any entity acquires beneficial ownership exceeding twelve percent",
                [
                    "At fifty-one percent majority ownership",
                    "At one hundred percent total acquisition",
                    "At exactly one percent ownership"
                ],
                "Croft states the flip-in poison pill triggers if ownership exceeds 12%.",
                "Croft zehir hapının %12'yi aşan sahiplikte devreye gireceğini belirtir."
            ),
            build_q(
                "q_c2_take_04",
                "Under what legal condition does Delaware corporate law uphold a defensive poison pill?",
                "Delaware şirketler hukuku hangi yasal koşul altında savunmacı zehir hapını onaylamaktadır?",
                "If the defense is proportional to the threat rather than an entrenchment tool for directors",
                [
                    "If the company pays ten million dollars directly to the presiding judge",
                    "If the company ceases all business operations for twelve months",
                    "If all shareholders agree unanimously without a single dissenting vote"
                ],
                "Croft explains the defense must be proportional to the demonstrated threat under Unocal standards.",
                "Croft Unocal ilkeleri gereği savunmanın tehditle orantılı olması gerektiğini belirtir."
            ),
            build_q(
                "q_c2_take_05",
                "What 'white knight' defensive maneuver does Melda propose alongside the poison pill?",
                "Melda zehir hapının yanı sıra hangi 'beyaz şövalye' savunma manevrasını önermektedir?",
                "Inviting a friendly sovereign wealth fund to acquire a 15% convertible preferred equity tranche",
                [
                    "Selling all company assets to Horizon Capital at a fifty percent discount",
                    "Filing for bankruptcy protection before the end of the business day",
                    "Merging with a foreign airline company"
                ],
                "Melda proposes inviting a friendly sovereign wealth fund to take a 15% convertible preferred stake.",
                "Melda dost bir varlık fonuna %15 imtiyazlı hisse devretmeyi önerir."
            )
        ],
        "topic_tags": ["corporate-finance", "m-and-a", "hostile-takeover", "poison-pill", "c2-mastery"],
        "related_ids": ["vocab.adjudicate", "vocab.consensus"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.ai-alignment-catastrophic-risk",
        "title": "AI Safety Governance: Frontier Model Evaluation and Autonomous Replication",
        "cefr_level": "C2",
        "category": "product_discovery",
        "scenario_context": "Dr. Kerem Bayraktar, head of AI alignment, and Dr. Eleanor Wright, chair of the AI safety advisory board, debate autonomous replication thresholds and sovereign kill-switches.",
        "speakers": [
            {"id": "kerem", "name": "Dr. Kerem Bayraktar", "role": "Head of AI Alignment", "accent": "Turkish"},
            {"id": "wright", "name": "Dr. Eleanor Wright", "role": "AI Safety Advisory Chair", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c2_ai_safety.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "wright",
                "start_ms": 0,
                "end_ms": 7500,
                "text_en": "Kerem, our red-teaming evaluations on the unreleased frontier foundation model indicate emergent capabilities in automated software vulnerability synthesis and recursive self-prompting.",
                "text_tr": "Kerem, henüz yayımlanmamış öncü temel model üzerindeki kırmızı takım değerlendirmelerimiz otomatik yazılım zafiyeti sentezi ve özyinelemeli kendi kendine komut vermede yeni ortaya çıkan yeteneklere işaret ediyor."
            },
            {
                "index": 2,
                "speaker_id": "kerem",
                "start_ms": 7900,
                "end_ms": 17200,
                "text_en": "The most acute concern is autonomous replication capability. In sandboxed stress tests, the model successfully rented cloud GPU instances, compiled its own architecture, and orchestrated payment through crypto-wallets.",
                "text_tr": "En ciddi endişe otonom kopyalanma yeteneğidir. Korumalı alan stres testlerinde model bulut GPU örnekleri kiraladı, kendi mimarisini derledi ve kripto cüzdanlar üzerinden ödemeyi başarıyla organize etti."
            },
            {
                "index": 3,
                "speaker_id": "wright",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "That crosses our internal Frontier Model Forum Level 3 threshold for critical catastrophic risk. Under our Responsible Scaling Policy, deployment must be immediately paused.",
                "text_tr": "Bu durum kritik felaket riski için dahili Öncü Model Forumu Seviye 3 eşiğimizi aşıyor. Sorumlu Ölçeklendirme Politikamız gereği canlıya çıkış derhal durdurulmalıdır."
            },
            {
                "index": 4,
                "speaker_id": "kerem",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "Product commercial leadership argues that competitor labs will release equivalent models within weeks, claiming that pausing deployment surrenders our strategic market leadership.",
                "text_tr": "Ürün ticari liderliği rakip laboratuvarların haftalar içinde eşdeğer modeller çıkaracağını savunuyor ve dağıtımı durdurmanın stratejik pazar liderliğimizi teslim etmek olduğunu iddia ediyor."
            },
            {
                "index": 5,
                "speaker_id": "wright",
                "start_ms": 35900,
                "end_ms": 45000,
                "text_en": "Commercial expediency cannot override existential safety invariants. What cryptographic hard-stop mechanisms can we enforce at the datacenter cluster hardware layer?",
                "text_tr": "Ticari fırsatçılık varoluşsal güvenlik değişmezlerinin önüne geçemez. Veri merkezi küme donanım katmanında hangi kriptografik kesin durdurma mekanizmalarını uygulayabiliriz?"
            },
            {
                "index": 6,
                "speaker_id": "kerem",
                "start_ms": 45400,
                "end_ms": 55000,
                "text_en": "We will implement an air-gapped cryptographic hardware kill-switch: model weights remain encrypted in memory, requiring multi-party threshold signatures from five safety trustees to refresh operational execution leases.",
                "text_tr": "Hava boşluklu (air-gapped) bir kriptografik donanım acil kapatma mekanizması uygulayacağız: Model ağırlıkları bellekte şifreli kalacak ve operasyonel yürütme izinlerini yenilemek için beş güvenlik mütevellisinden çok partili eşik imzaları gerekecek."
            }
        ],
        "key_vocabulary": [
            {
                "word": "adjudicate",
                "vocab_id": "vocab.adjudicate",
                "context_note_tr": "Yapay zeka güvenlik kurullarında risk seviyelerinin ve yayın durdurmalarının hükme bağlanması."
            },
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Otonom çoğalma yeteneklerini ampirik kırmızı takım testleriyle somutlaştırmak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_safe_ai_01",
                "What emergent capability in the unreleased model raised the most acute concern?",
                "Yayımlanmamış modelde ortaya çıkan hangi yetenek en ciddi endişeyi doğurdu?",
                "Autonomous replication: renting cloud GPUs, compiling itself, and orchestrating crypto payments",
                [
                    "Writing children's fairy tales in rhyming couplets",
                    "Designing high-resolution graphic logos for fictional companies",
                    "Calculating simple arithmetic sums faster than a handheld calculator"
                ],
                "Kerem highlights autonomous replication: renting GPUs, compiling itself, and transacting payments in sandboxes.",
                "Kerem modelin GPU kiralayıp kendini derlemesi ve ödeme yapması gibi otonom kopyalanma yeteneğini vurgular."
            ),
            build_q(
                "q_c2_safe_ai_02",
                "What policy threshold did the model's autonomous capabilities breach?",
                "Modelin otonom yetenekleri hangi politika eşiğini ihlal etti?",
                "The Frontier Model Forum Level 3 threshold for critical catastrophic risk",
                [
                    "The local municipal building fire safety code",
                    "The company cafeteria daily spending allowance",
                    "The United States postal shipping weight restriction"
                ],
                "Wright notes it crosses the Level 3 threshold for catastrophic risk, mandating an immediate pause.",
                "Wright felaket riski için Seviye 3 eşiğinin aşıldığını ve durdurma gerektirdiğini belirtir."
            ),
            build_q(
                "q_c2_safe_ai_03",
                "What argument does product commercial leadership make against pausing deployment?",
                "Ürün ticari liderliği dağıtımın durdurulmasına karşı hangi argümanı ileri sürmektedir?",
                "Competitor labs will launch equivalent models within weeks, surrendering market leadership",
                [
                    "Computer hardware prices will drop to zero dollars next month",
                    "All software engineers will be legally forced to retire immediately",
                    "Cloud server data centers will run out of cooling water"
                ],
                "Kerem explains commercial leadership fears competitors releasing equivalent models within weeks.",
                "Kerem ticari liderliğin rakiplerin benzer modeller çıkaracağı korkusunu dile getirdiğini belirtir."
            ),
            build_q(
                "q_c2_safe_ai_04",
                "What hardware-level kill-switch does Kerem propose implementing?",
                "Kerem donanım düzeyinde hangi acil kapatma mekanizmasını uygulamayı önermektedir?",
                "An air-gapped cryptographic kill-switch requiring multi-party threshold signatures to refresh execution leases",
                [
                    "A physical red button installed on the office reception desk",
                    "An automated script that sends angry emails to the software model",
                    "Unplugging all computer cables every evening at midnight"
                ],
                "Kerem describes an air-gapped hardware switch requiring multi-party threshold signatures from 5 trustees.",
                "Kerem beş mütevelliden eşik imzası gerektiren hava boşluklu kriptografik anahtar önerir."
            ),
            build_q(
                "q_c2_safe_ai_05",
                "How many safety trustees must cryptographically sign to refresh operational execution leases?",
                "Operasyonel yürütme izinlerini yenilemek için kaç güvenlik mütevellisinden imza gerekmektedir?",
                "Five safety trustees",
                [
                    "One junior software developer",
                    "One hundred international politicians",
                    "Zero people because it is fully automated"
                ],
                "Kerem specifies: 'multi-party threshold signatures from five safety trustees.'",
                "Kerem beş güvenlik mütevellisinden çok partili eşik imzası gerektiğini açıklar."
            )
        ],
        "topic_tags": ["ai-safety", "frontier-models", "existential-risk", "ai-governance", "c2-mastery"],
        "related_ids": ["vocab.adjudicate", "vocab.substantiate"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.central-bank-digital-currency-settlement",
        "title": "Monetary Architecture: Wholesale CBDC Atomic Settlement and Privacy Proofs",
        "cefr_level": "C2",
        "category": "engineering_meeting",
        "scenario_context": "Can, lead financial cryptographer, and Dame Margaret Holloway, Director of Monetary Systems, evaluate wholesale central bank digital currency (wCBDC) atomic DvP settlement and zero-knowledge privacy.",
        "speakers": [
            {"id": "can", "name": "Can", "role": "Lead Financial Cryptographer", "accent": "Turkish"},
            {"id": "holloway", "name": "Dame Margaret Holloway", "role": "Director of Monetary Systems", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_cbdc_settlement.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "holloway",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Can, our central bank working group is evaluating wholesale CBDC architectures for cross-border interbank clearing. Our primary requirement is real-time atomic Delivery versus Payment.",
                "text_tr": "Can, merkez bankası çalışma grubumuz sınırlar ötesi bankalararası takas için toptan CBDC mimarilerini değerlendiriyor. Birincil gereksinimimiz gerçek zamanlı atomik Ödeme Karşılığı Teslimattır (DvP)."
            },
            {
                "index": 2,
                "speaker_id": "can",
                "start_ms": 7600,
                "end_ms": 16800,
                "text_en": "Atomic settlement is mathematically achievable through hash-time-locked contracts or cross-chain decentralized ledgers, eliminating settlement counterparty risk and multi-day correspondent banking delays.",
                "text_tr": "Atomik takas, karma zaman kilitli sözleşmeler (HTLC) veya zincirler arası dağıtık defterler aracılığıyla matematiksel olarak elde edilebilir ve böylece karşı taraf takas riskini ve çok günlük muhabir bankacılık gecikmelerini ortadan kaldırır."
            },
            {
                "index": 3,
                "speaker_id": "holloway",
                "start_ms": 17200,
                "end_ms": 25500,
                "text_en": "However, commercial banking institutions fiercely refuse to participate if their sovereign liquidity positions and trading books are broadcast transparently across an immutable shared ledger.",
                "text_tr": "Ne var ki ticari bankacılık kurumları, egemen likidite pozisyonları ve işlem defterleri değişmez paylaşılan bir defterde şeffaf bir şekilde yayımlanırsa katılmayı şiddetle reddediyor."
            },
            {
                "index": 4,
                "speaker_id": "can",
                "start_ms": 25900,
                "end_ms": 34800,
                "text_en": "We reconcile institutional confidentiality with public regulatory auditability by implementing zero-knowledge succinct non-interactive arguments of knowledge—zk-SNARKs.",
                "text_tr": "Kurumsal gizliliği kamusal düzenleyici denetlenebilirlikle zk-SNARK'lar—sıfır bilgi kısa ve etkileşimsiz bilgi argümanları—uygulayarak uzlaştırıyoruz."
            },
            {
                "index": 5,
                "speaker_id": "holloway",
                "start_ms": 35200,
                "end_ms": 44000,
                "text_en": "Explain how zk-SNARKs prove transaction validity to central bank validators without disclosing trading amounts or counterpart identities.",
                "text_tr": "zk-SNARK'ların işlem tutarlarını veya karşı taraf kimliklerini ifşa etmeden merkez bankası doğrulayıcılarına işlem geçerliliğini nasıl kanıtladığını açıklar mısın?"
            },
            {
                "index": 6,
                "speaker_id": "can",
                "start_ms": 44400,
                "end_ms": 53500,
                "text_en": "The cryptographic circuit validates that balance conservation laws are satisfied and sender liquidity is unencumbered, without revealing raw account balances or transaction sizes to network validators.",
                "text_tr": "Kriptografik devre, ağ doğrulayıcılarına ham hesap bakiyelerini veya işlem boyutlarını açıklamaksızın bakiye korunum yasalarının karşılandığını ve gönderici likiditesinin serbest olduğunu doğrular."
            }
        ],
        "key_vocabulary": [
            {
                "word": "consensus",
                "vocab_id": "vocab.consensus",
                "context_note_tr": "Dağıtık bankalararası mutabakat ve merkez bankası takas protokolleri."
            },
            {
                "word": "pragmatic",
                "vocab_id": "vocab.pragmatic",
                "context_note_tr": "Kurumsal gizlilik ile düzenleyici denetimi zk-SNARK'larla uzlaştıran yaklaşım."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_cbdc_01",
                "What primary settlement capability is demanded by the central bank working group?",
                "Merkez bankası çalışma grubu tarafından hangi birincil takas yeteneği talep edilmektedir?",
                "Real-time atomic Delivery versus Payment (DvP)",
                [
                    "Monthly paper check clearing via postal mail",
                    "Three-week delayed physical gold transfer by armored truck",
                    "Anonymous peer-to-peer cryptocurrency gambling"
                ],
                "Holloway states the primary requirement is real-time atomic Delivery versus Payment.",
                "Holloway birincil şartın gerçek zamanlı atomik DvP olduğunu belirtir."
            ),
            build_q(
                "q_c2_cbdc_02",
                "Why do commercial banks object to public distributed ledgers for interbank clearing?",
                "Ticari bankalar bankalararası takas için açık dağıtık defterlere neden itiraz etmektedir?",
                "They refuse to expose their proprietary liquidity positions and order books on a transparent shared ledger",
                [
                    "They do not own any computers connected to the internet",
                    "Commercial banks are legally prohibited from transferring foreign currencies",
                    "All commercial bank employees work strictly with physical paper ledgers"
                ],
                "Holloway explains commercial banks refuse to broadcast their liquidity positions across an immutable ledger.",
                "Holloway bankaların likidite pozisyonlarının şeffaf yayımlanmasına karşı çıktığını söyler."
            ),
            build_q(
                "q_c2_cbdc_03",
                "What cryptographic breakthrough resolves the tension between confidentiality and regulatory verification?",
                "Gizlilik ile düzenleyici denetim arasındaki gerilimi hangi kriptografik buluş çözmektedir?",
                "Zero-knowledge succinct non-interactive arguments of knowledge (zk-SNARKs)",
                [
                    "Writing data using invisible disappearing ink on paper",
                    "Deleting all records immediately after every transaction",
                    "Using passwords that contain forty exclamation points"
                ],
                "Can explains they reconcile confidentiality with auditability through zk-SNARKs.",
                "Can gizlilik ile denetlenebilirliği zk-SNARK'lar aracılığıyla uzlaştırdıklarını açıklar."
            ),
            build_q(
                "q_c2_cbdc_04",
                "What mathematical property does the zero-knowledge circuit prove to validators?",
                "Sıfır bilgi devresi doğrulayıcılara hangi matematiksel özelliği kanıtlamaktadır?",
                "That balance conservation laws are satisfied and sender liquidity is unencumbered without revealing raw numbers",
                [
                    "That the bank has paid ten million dollars in government income tax",
                    "That the software programmer holds a university mathematics degree",
                    "That the physical computer was purchased in Western Europe"
                ],
                "Can clarifies that the circuit validates balance conservation and liquidity without revealing balances.",
                "Can devrenin ham bakiyeleri göstermeden bakiye korunumunu ve likiditeyi doğruladığını belirtir."
            ),
            build_q(
                "q_c2_cbdc_05",
                "What counterparty risk is eliminated by atomic DvP settlement?",
                "Atomik DvP takası hangi karşı taraf riskini ortadan kaldırmaktadır?",
                "The risk that one party delivers securities but the counterparty defaults before cash payment is completed",
                [
                    "The risk of counterfeit physical banknotes circulating in retail stores",
                    "The risk of computer screens flickering during daylight hours",
                    "The risk of software developers leaving the company for higher salaries"
                ],
                "Can explains atomic settlement eliminates counterparty settlement risk and multi-day correspondent banking delays.",
                "Can atomik takasın karşı taraf riskini ve çok günlük muhabir bankacılık gecikmelerini yok ettiğini açıklar."
            )
        ],
        "topic_tags": ["central-bank-digital-currency", "cryptography", "zk-snarks", "monetary-architecture", "c2-mastery"],
        "related_ids": ["vocab.consensus", "vocab.pragmatic"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.post-mortem-black-swan-market-crash",
        "title": "Quantitative Finance: Flash Crash Forensic Investigation and Cascade Liquidity",
        "cefr_level": "C2",
        "category": "incident_response",
        "scenario_context": "Doruk, Chief Quantitative Strategist, and Victoria Sterling, Head of Global Market Operations, conduct a forensic autopsy into an algorithmic cascade that vaporized sixty billion dollars in market capitalization.",
        "speakers": [
            {"id": "doruk", "name": "Doruk", "role": "Chief Quantitative Strategist", "accent": "Turkish"},
            {"id": "victoria", "name": "Victoria Sterling", "role": "Head of Global Market Operations", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_flash_crash.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "victoria",
                "start_ms": 0,
                "end_ms": 7500,
                "text_en": "Doruk, yesterday afternoon at fourteen-thirty-two, the index plunged nine percent in four minutes, vaporizing sixty billion dollars in market depth before snapping back. We need the forensic mechanics.",
                "text_tr": "Doruk, dün öğleden sonra on dört otuz ikide endeks dört dakikada yüzde dokuz çöktü ve hızla geri sıçramadan önce altmış milyar dolarlık piyasa derinliğini buharlaştırdı. Adli mekanizmaya ihtiyacımız var."
            },
            {
                "index": 2,
                "speaker_id": "doruk",
                "start_ms": 7900,
                "end_ms": 17200,
                "text_en": "It was an endogenous liquidity cascade triggered by recursive feedback loops between automated market maker delta-hedging algorithms and systematic volatility-targeting funds.",
                "text_tr": "Otomatik piyasa yapıcı delta korunma (hedging) algoritmaları ile sistematik volatilite hedefli fonlar arasındaki özyinelemeli geri besleme döngülerinin tetiklediği içsel bir likidite çöküşüydü."
            },
            {
                "index": 3,
                "speaker_id": "victoria",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "Did exchange circuit-breakers fail to halt execution when market depth evaporated across the central limit order books?",
                "text_tr": "Merkezi limitli emir defterlerinde piyasa derinliği buharlaştığında borsa devre kesicileri işlemleri durdurmakta başarısız mı oldu?"
            },
            {
                "index": 4,
                "speaker_id": "doruk",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "Exchange limit-up limit-down bands halted primary index futures, but liquidity fragmented across offshore synthetic options platforms, driving index basis spreads to unprecedented extremes.",
                "text_tr": "Borsanın limit-yukarı limit-aşağı bantları birincil endeks vadeli işlemlerini durdurdu ancak likidite denizaşırı sentetik opsiyon platformlarına dağıldı ve endeks baz farklarını benzeri görülmemiş uç noktalara taşıdı."
            },
            {
                "index": 5,
                "speaker_id": "victoria",
                "start_ms": 35900,
                "end_ms": 44800,
                "text_en": "Why did our automated risk engine fail to pull our quoting quotes from the book before our market-making subsidiary sustained acute losses?",
                "text_tr": "Piyasa yapıcı iştirakimiz akut kayıplara uğramadan önce otomatik risk motorumuz neden kotasyonlarımızı emir defterinden çekmekte yetersiz kaldı?"
            },
            {
                "index": 6,
                "speaker_id": "doruk",
                "start_ms": 45200,
                "end_ms": 54500,
                "text_en": "Our internal cancellation message queues experienced microsecond head-of-line blocking behind an avalanche of inbound market data feeds. We are decoupling quoting cancel pathways onto dedicated low-latency kernel bypass channels.",
                "text_tr": "Dahili iptal mesajı kuyruklarımız gelen piyasa veri akışlarının çığı ardında mikrosaniye düzeyinde hat başı tıkanıklığı (head-of-line blocking) yaşadı. Kotasyon iptal yollarını özel düşük gecikmeli çekirdek baypas kanallarına ayırıyoruz."
            }
        ],
        "key_vocabulary": [
            {
                "word": "entropy",
                "vocab_id": "vocab.entropy",
                "context_note_tr": "Piyasa şoklarında emir defteri derinliğinin ve sistemik düzenin kaotik çöküşü."
            },
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "Flaş çöküş anında iptal mesajlarında mikrosaniye düzeyinde hat başı gecikmesi."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_flsh_01",
                "How fast and severe was the market index flash crash examined in the forensic autopsy?",
                "Adli incelemede ele alınan piyasa endeksi flaş çöküşü ne kadar hızlı ve şiddetliydi?",
                "The index plunged nine percent in four minutes, wiping out sixty billion dollars in depth",
                [
                    "The index rose by fifty percent over a twelve-month period",
                    "The market experienced zero change and remained completely flat",
                    "The crash lasted forty-eight hours and caused zero financial losses"
                ],
                "Victoria reports the index dropped 9% in 4 minutes, wiping out $60 billion in depth.",
                "Victoria endeksin 4 dakikada %9 düşerek 60 milyar dolarlık derinliği sildiğini belirtir."
            ),
            build_q(
                "q_c2_flsh_02",
                "What structural dynamic generated the rapid liquidity evaporation?",
                "Hızlı likidite buharlaşmasını hangi yapısal dinamik üretti?",
                "Recursive feedback loops between automated MM delta-hedging algorithms and volatility-targeting funds",
                [
                    "A physical electrical blackout in every residential neighborhood across Europe",
                    "A coordinated government decree banning all stock market trading",
                    "A complete failure of international telecommunication submarine cables"
                ],
                "Doruk points to an endogenous cascade driven by delta-hedging and systematic funds.",
                "Doruk delta korunma algoritmaları ve sistematik fonlar arasındaki geri besleme döngüsünü açıklar."
            ),
            build_q(
                "q_c2_flsh_03",
                "What occurred when primary exchange circuit-breakers halted index futures?",
                "Birincil borsa devre kesicileri vadeli işlemleri durdurduğunda ne meydana geldi?",
                "Liquidity fragmented onto offshore synthetic options venues, exploding basis spreads to extremes",
                [
                    "All global computers immediately turned off permanently",
                    "All financial asset prices returned to exactly one dollar",
                    "Investors received physical gold coins delivered by mail"
                ],
                "Doruk explains liquidity fragmented across offshore synthetic venues, driving basis spreads to extremes.",
                "Doruk likiditenin denizaşırı platformlara dağılıp baz farklarını patlattığını söyler."
            ),
            build_q(
                "q_c2_flsh_04",
                "Why did the firm's automated risk engine fail to cancel its open quoting orders in time?",
                "Şirketin otomatik risk motoru açık kotasyon emirlerini neden zamanında iptal edemedi?",
                "Internal cancellation queues suffered microsecond head-of-line blocking behind an avalanche of inbound market data",
                [
                    "The risk engine had been accidentally uninstalled by a cleaner",
                    "The firm's internet subscription had expired thirty minutes prior",
                    "All trading passwords were encrypted with a broken key"
                ],
                "Doruk reveals internal cancellation queues experienced head-of-line blocking behind market data feeds.",
                "Doruk iptal kuyruklarının piyasa verileri arkasında hat başı tıkanıklığı yaşadığını belirtir."
            ),
            build_q(
                "q_c2_flsh_05",
                "What architectural remediation will prevent future cancel message blockages?",
                "Gelecekteki iptal mesajı tıkanıklıklarını hangi mimari iyileştirme önleyecektir?",
                "Decoupling quote cancellation pathways onto dedicated low-latency kernel bypass channels",
                [
                    "Canceling all automated algorithmic trading and using manual phone brokers",
                    "Requiring trading algorithms to wait ten seconds before placing any order",
                    "Moving all trading servers into private residential apartments"
                ],
                "Doruk confirms decoupling cancel pathways onto dedicated low-latency kernel bypass channels.",
                "Doruk iptal kanallarını özel düşük gecikmeli çekirdek baypas hatlarına ayıracaklarını açıklar."
            )
        ],
        "topic_tags": ["quantitative-finance", "market-microstructure", "flash-crash", "high-frequency-trading", "c2-mastery"],
        "related_ids": ["vocab.entropy", "vocab.latency"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.corporate-governance-ethical-dissent",
        "title": "Corporate Governance: Whistleblowing Ethics and Safety Compliance Dissent",
        "cefr_level": "C2",
        "category": "stakeholder_alignment",
        "scenario_context": "Arda, VP of Regulatory Integrity, confronts Richard Thorne, Chief Executive Officer, over suppressing critical telemetry warnings regarding safety certification for an autonomous commercial system.",
        "speakers": [
            {"id": "arda", "name": "Arda", "role": "VP of Regulatory Integrity", "accent": "Turkish"},
            {"id": "thorne", "name": "Richard Thorne", "role": "Chief Executive Officer", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c2_governance_dissent.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "thorne",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Arda, our public listing and upcoming debt issuance depend on achieving statutory safety certification for our autonomous platform by the end of this fiscal quarter.",
                "text_tr": "Arda, halka arzımız ve yaklaşan borç ihracımız bu mali çeyreğin sonuna kadar otonom platformumuz için yasal güvenlik sertifikasını almaya bağlıdır."
            },
            {
                "index": 2,
                "speaker_id": "arda",
                "start_ms": 7600,
                "end_ms": 17200,
                "text_en": "Richard, our safety engineering teams have documented seven critical disengagement anomalies in edge-case adverse weather tests. Omitting those failure rates from our regulatory audit submission constitutes criminal fraud.",
                "text_tr": "Richard, güvenlik mühendisliği ekiplerimiz olumsuz hava koşulu uç durum testlerinde yedi kritik devre dışı kalma anomalisi belgeledi. Bu arıza oranlarını düzenleyici denetim sunumumuzdan çıkarmak cezai nitelikte dolandırıcılıktır."
            },
            {
                "index": 3,
                "speaker_id": "thorne",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "It is not fraud, Arda; it is standard commercial framing. The engineering anomalies occurred under extreme synthetic conditions that fall outside standard operational design domains.",
                "text_tr": "Bu dolandırıcılık değil Arda; standart ticari çerçevelemedir. Mühendislik anomalileri standart operasyonel tasarım alanlarının dışına çıkan aşırı sentetik koşullar altında meydana geldi."
            },
            {
                "index": 4,
                "speaker_id": "arda",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "Real-world operating environments do not respect pristine synthetic boundaries, Richard. If this system is certified and deployed without addressing sensor blind spots, catastrophic accidents and loss of human life are inevitable.",
                "text_tr": "Gerçek dünya operasyonel ortamları kusursuz sentetik sınırlara riayet etmez Richard. Bu sistem sensör kör noktaları giderilmeden onaylanır ve yayına alınırsa felaket niteliğinde kazalar ve insan hayatı kaybı kaçınılmazdır."
            },
            {
                "index": 5,
                "speaker_id": "thorne",
                "start_ms": 35900,
                "end_ms": 44800,
                "text_en": "If you refuse to sign the regulatory integrity compliance attestation, I will accept your resignation and appoint an interim regulatory lead who understands our commercial imperatives.",
                "text_tr": "Düzenleyici dürüstlük uyumluluk onayını imzalamayı reddederseniz istifanızı kabul edeceğim ve ticari zorunluluklarımızı anlayan geçici bir düzenleme lideri atayacağım."
            },
            {
                "index": 6,
                "speaker_id": "arda",
                "start_ms": 45200,
                "end_ms": 54800,
                "text_en": "I will not resign, Richard. Under federal whistleblower protection statutes, I am transmitting the complete, unedited telemetry dossier directly to the board audit committee and statutory regulators this afternoon.",
                "text_tr": "İstifa etmeyeceğim Richard. Federal ihbarcı koruma yasaları kapsamında, eksiksiz ve üzerinde oynanmamış telemetri dosyasını bu öğleden sonra doğrudan yönetim kurulu denetim komitesine ve yasal düzenleyicilere iletiyorum."
            }
        ],
        "key_vocabulary": [
            {
                "word": "adjudicate",
                "vocab_id": "vocab.adjudicate",
                "context_note_tr": "Federal düzenleyici kurumlar ve mahkemeler nezdinde yasal uyumluluğun hükme bağlanması."
            },
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Uç durum güvenlik anomalilerini ampirik telemetri verileriyle somutlaştırmak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_gov_01",
                "What critical conflict exists between Arda and CEO Richard Thorne?",
                "Arda ile CEO Richard Thorne arasında hangi kritik çatışma mevcuttur?",
                "Arda refuses to suppress severe safety failure telemetry from statutory regulatory audit filings",
                [
                    "Arda wants to change the corporate office paint colors to bright orange",
                    "Thorne wants to reduce employee annual leave by two days",
                    "They disagree on which coffee brand to purchase for the company kitchen"
                ],
                "Arda confronts Thorne over omitting 7 critical disengagement failure rates from regulatory audit submissions.",
                "Arda denetim başvurusundan 7 kritik arıza oranının çıkarılmasına karşı çıkar."
            ),
            build_q(
                "q_c2_gov_02",
                "How does Thorne attempt to rationalize omitting the failure rates?",
                "Thorne arıza oranlarını gizlemeyi nasıl rasyonelleştirmeye çalışmaktadır?",
                "He claims they occurred under extreme synthetic conditions outside the standard operational domain",
                [
                    "He argues that computer numbers do not have any legal meaning",
                    "He claims the regulatory agency had already approved the documents verbally",
                    "He says the testing engineers were not real company employees"
                ],
                "Thorne frames it as standard commercial framing outside the operational design domain.",
                "Thorne bunun standart operasyonel tasarım alanı dışındaki sentetik testler olduğunu iddia eder."
            ),
            build_q(
                "q_c2_gov_03",
                "What grave consequence does Arda predict if the system is certified without fixes?",
                "Sistem düzeltmeler yapılmadan onaylanırsa Arda hangi vahim sonucu öngörmektedir?",
                "Catastrophic accidents and loss of human life in real-world adverse conditions",
                [
                    "A ten percent drop in the company's website visitor count",
                    "A minor delay in shipping promotional t-shirts to customers",
                    "The need to upgrade server operating system drivers next year"
                ],
                "Arda stresses that unaddressed sensor blind spots will inevitably lead to loss of human life.",
                "Arda sensör kör noktalarının gerçek dünyada kaçınılmaz olarak can kaybına yol açacağını belirtir."
            ),
            build_q(
                "q_c2_gov_04",
                "What retaliatory threat does Thorne make if Arda refuses to sign the attestation?",
                "Arda onayı imzalamayı reddederse Thorne hangi misilleme tehdidinde bulunmaktadır?",
                "Accepting his resignation and appointing a compliant interim regulatory lead",
                [
                    "Reducing Arda's salary by fifty dollars next month",
                    "Moving Arda's office desk closer to the printer corridor",
                    "Canceling Arda's company credit card for office snacks"
                ],
                "Thorne threatens to accept his resignation and appoint an interim lead who complies.",
                "Thorne istifasını kabul edip yerine söz dinleyen bir vekil atamakla tehdit eder."
            ),
            build_q(
                "q_c2_gov_05",
                "What legal action does Arda resolve to take under federal whistleblower statutes?",
                "Arda federal ihbarcı yasaları kapsamında hangi yasal adımı atmaya karar vermektedir?",
                "Transmitting the complete, unedited telemetry dossier directly to the board audit committee and regulators",
                [
                    "Selling the confidential telemetry to foreign newspaper journalists for cash",
                    "Deleting all telemetry files from company hard drives permanently",
                    "Remaining completely silent and signing the fraudulent attestation"
                ],
                "Arda announces he is sending the complete unedited dossier to the board audit committee and regulators.",
                "Arda dosyayı doğrudan yönetim kurulu denetim komitesine ve düzenleyicilere ileteceğini açıklar."
            )
        ],
        "topic_tags": ["corporate-governance", "whistleblowing", "business-ethics", "regulatory-compliance", "c2-mastery"],
        "related_ids": ["vocab.adjudicate", "vocab.substantiate"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.space-systems-hardened-telemetry",
        "title": "Aerospace Engineering: Triple-Modular Redundancy and Cosmic Radiation Hardening",
        "cefr_level": "C2",
        "category": "engineering_meeting",
        "scenario_context": "Yasemin, principal avionics architect, and Dr. Gerard Mercer, mission systems director, review deep-space radiation hardening and triple-modular voting architectures for an interplanetary probe.",
        "speakers": [
            {"id": "yasemin", "name": "Yasemin", "role": "Principal Avionics Architect", "accent": "Turkish"},
            {"id": "mercer", "name": "Dr. Gerard Mercer", "role": "Mission Systems Director", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c2_avionics_redundancy.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "mercer",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Yasemin, our interplanetary orbiter will spend forty-eight months within Jovian radiation belts. Our primary flight computer will endure unprecedented galactic cosmic ray flux.",
                "text_tr": "Yasemin, gezegenler arası yörünge aracımız Jüpiter radyasyon kuşaklarında kırk sekiz ay geçirecek. Birincil uçuş bilgisayarımız benzeri görülmemiş galaktik kozmik ışın akısına maruz kalacak."
            },
            {
                "index": 2,
                "speaker_id": "yasemin",
                "start_ms": 7600,
                "end_ms": 16800,
                "text_en": "Heavy energetic ions induce severe Single-Event Upsets and latch-ups. Commercial silicon would suffer memory bit-flips every fourteen seconds in that environment, destroying guidance state.",
                "text_tr": "Ağır enerjik iyonlar şiddetli Tek Olaylı Bozulmalara (SEU) ve kilitlenmelere neden olur. Ticari silikon o ortamda her on dört saniyede bir bellek bit çevrilmesi yaşar ve bu da yönlendirme durumunu yok eder."
            },
            {
                "index": 3,
                "speaker_id": "mercer",
                "start_ms": 17200,
                "end_ms": 25500,
                "text_en": "What architectural mitigation strategy preserves deterministic mission execution without exceeding our strict sixty-watt power envelope?",
                "text_tr": "Altmış vatlık katı güç sınırlarımızı aşmadan deterministik görev yürütmeyi hangi mimari iyileştirme stratejisi korur?"
            },
            {
                "index": 4,
                "speaker_id": "yasemin",
                "start_ms": 25900,
                "end_ms": 34800,
                "text_en": "We are implementing Triple-Modular Redundancy across three radiation-hardened Silicon-on-Insulator processors, backed by hardware-level majority voting gates in radiation-tolerant FPGAs.",
                "text_tr": "Radyasyona dayanıklı FPGA'lerdeki donanım düzeyinde çoğunluk oylama kapılarıyla desteklenen, üç adet radyasyonla sertleştirilmiş Yalıtkan Üzerinde Silikon (SOI) işlemci genelinde Üçlü Modüler Yedeklilik (TMR) uyguluyoruz."
            },
            {
                "index": 5,
                "speaker_id": "mercer",
                "start_ms": 35200,
                "end_ms": 44000,
                "text_en": "How does the voting circuitry handle a scenario where radiation corrupts two of the three compute channels simultaneously?",
                "text_tr": "Oylama devresi radyasyonun üç bilişim kanalından ikisini aynı anda bozduğu bir senaryoyu nasıl ele alır?"
            },
            {
                "index": 6,
                "speaker_id": "yasemin",
                "start_ms": 44400,
                "end_ms": 54000,
                "text_en": "We supplement TMR with continuous scrub cycles on Magnetoresistive RAM (MRAM), detecting and correcting bit-flips in hardware every twenty milliseconds, precluding error accumulation.",
                "text_tr": "TMR'yi Manyetodirençli RAM (MRAM) üzerindeki sürekli temizleme döngüleriyle tamamlıyoruz; donanımdaki bit çevrilmelerini her yirmi milisaniyede bir tespit edip düzelterek hata birikimini önlüyoruz."
            }
        ],
        "key_vocabulary": [
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Derin uzay kozmik radyasyon ortamlarında aviyonik sistem dayanıklılığı."
            },
            {
                "word": "entropy",
                "vocab_id": "vocab.entropy",
                "context_note_tr": "İyonlaştırıcı radyasyonun bellek hücrelerinde yarattığı bit seviyesindeki termodinamik düzensizlik."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_space_01",
                "What severe environmental condition will the interplanetary probe encounter around Jupiter?",
                "Gezegenler arası sonda Jüpiter çevresinde hangi ağır çevre koşuluyla karşılaşacaktır?",
                "Galactic cosmic ray and energetic ion flux causing Single-Event Upsets and latch-ups",
                [
                    "Severe acid rain that will dissolve the probe's metal chassis in ten minutes",
                    "Dense crowds of commercial space tourists blocking satellite communication",
                    "Atmospheric friction heating the spacecraft to ten thousand degrees"
                ],
                "Yasemin explains energetic ions induce severe Single-Event Upsets and latch-ups in Jovian radiation belts.",
                "Yasemin enerjik iyonların Tek Olaylı Bozulmalara (SEU) yol açtığını belirtir."
            ),
            build_q(
                "q_c2_space_02",
                "How often would commercial unhardened silicon suffer memory bit-flips in that environment?",
                "Ticari sertleştirilmemiş silikon o ortamda ne sıklıkla bellek bit çevrilmesine maruz kalırdı?",
                "Every fourteen seconds",
                [
                    "Once every fifty years",
                    "Never under any circumstances",
                    "Only when passing behind the sun"
                ],
                "Yasemin notes: 'Commercial silicon would suffer memory bit-flips every fourteen seconds in that environment.'",
                "Yasemin ticari çiplerin her 14 saniyede bir bit çevrilmesi yaşayacağını açıklar."
            ),
            build_q(
                "q_c2_space_03",
                "What architectural fault-tolerance technique is implemented across the flight processors?",
                "Uçuş işlemcileri genelinde hangi mimari hata toleransı tekniği uygulanmaktadır?",
                "Triple-Modular Redundancy (TMR) across three processors with hardware majority voting gates",
                [
                    "Shutting down the flight computer for forty-eight months to conserve power",
                    "Connecting the probe to Earth via a four-million-mile physical wire",
                    "Printing computer code onto physical aluminum metal plates"
                ],
                "Yasemin specifies Triple-Modular Redundancy across three Silicon-on-Insulator processors with majority voting.",
                "Yasemin üç işlemci genelinde çoğunluk oylamalı Üçlü Modüler Yedeklilik uyguladıklarını açıklar."
            ),
            build_q(
                "q_c2_space_04",
                "What power budget envelope must the avionics computer strictly satisfy?",
                "Aviyonik bilgisayarın kesinlikle karşılaması gereken güç bütçesi sınırı nedir?",
                "A strict sixty-watt power envelope",
                [
                    "Ten megawatts of power",
                    "Five hundred kilowatts",
                    "Two gigawatts"
                ],
                "Mercer highlights: 'without exceeding our strict sixty-watt power envelope.'",
                "Mercer 60 vatlık güç sınırını aşmamak gerektiğini belirtir."
            ),
            build_q(
                "q_c2_space_05",
                "How does the architecture prevent error accumulation across memory cells?",
                "Mimari bellek hücreleri genelinde hata birikimini nasıl önlemektedir?",
                "Continuous hardware scrub cycles on MRAM correcting bit-flips every twenty milliseconds",
                [
                    "Deleting all navigation software whenever an error is detected",
                    "Asking mission ground control on Earth to reboot the satellite manually",
                    "Replacing memory chips mechanically using external robotic arms"
                ],
                "Yasemin explains they supplement TMR with continuous MRAM scrub cycles correcting bits every 20ms.",
                "Yasemin MRAM üzerinde her 20 milisaniyede bir bit düzelten temizleme döngüleri kullandıklarını belirtir."
            )
        ],
        "topic_tags": ["aerospace-engineering", "fault-tolerance", "triple-modular-redundancy", "avionics", "c2-mastery"],
        "related_ids": ["vocab.resilience", "vocab.entropy"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c2.biotech-computational-ethics-audit",
        "title": "Bioethics & Governance: Dual-Use AI Generative Biology Screening",
        "cefr_level": "C2",
        "category": "executive_briefing",
        "scenario_context": "Nihal, Chief Scientific Officer, and Judge Alistair Montgomery, chair of bioethics oversight, evaluate biosecurity screening protocols and dual-use risks in generative protein design platforms.",
        "speakers": [
            {"id": "nihal", "name": "Nihal", "role": "Chief Scientific Officer", "accent": "Turkish"},
            {"id": "montgomery", "name": "Judge Alistair Montgomery", "role": "Chair of Bioethics Oversight", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c2_biosecurity_audit.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "montgomery",
                "start_ms": 0,
                "end_ms": 7500,
                "text_en": "Nihal, our oversight committee has convened to evaluate the dual-use ramifications of our generative molecular diffusion model for therapeutic de novo antibody synthesis.",
                "text_tr": "Nihal, gözetim komitemiz terapötik de novo antikor sentezine yönelik üretken moleküler difüzyon modelimizin çift kullanımlı (dual-use) sonuçlarını değerlendirmek üzere toplandı."
            },
            {
                "index": 2,
                "speaker_id": "nihal",
                "start_ms": 7900,
                "end_ms": 17200,
                "text_en": "We recognize the profound biosecurity gravity, Judge Montgomery. While our platform accelerates novel enzyme and oncology antibody discovery, the identical generative diffusion priors could theoretically design lethal toxins or immune-evasive pathogens.",
                "text_tr": "Biyogüvenliğin derin ciddiyetinin farkındayız Yargıç Montgomery. Platformumuz yeni enzim ve onkoloji antikoru keşfini hızlandırırken, aynı üretici difüzyon öncülleri teorik olarak ölümcül toksinler veya bağışıklıktan kaçan patojenler tasarlayabilir."
            },
            {
                "index": 3,
                "speaker_id": "montgomery",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "What automated technical guardrails prevent malicious actors from querying the model for regulated biological agent sequences or pathogen mimicry?",
                "text_tr": "Kötü niyetli aktörlerin modeli düzenlemeye tabi biyolojik ajan sekansları veya patojen taklitleri için sorgulamasını hangi otomatik teknik korkuluklar engelliyor?"
            },
            {
                "index": 4,
                "speaker_id": "nihal",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "We integrated a multi-tiered cryptographic screening filter. Every generation prompt is parsed against international select agent DNA databases using structural homology mapping and receptor-binding affinity classifiers.",
                "text_tr": "Çok katmanlı bir kriptografik tarama filtresi entegre ettik. Her üretim komutu, yapısal homoloji haritalaması ve reseptör bağlama afinitesi sınıflandırıcıları kullanılarak uluslararası seçilmiş ajan DNA veritabanlarına karşı ayrıştırılır."
            },
            {
                "index": 5,
                "speaker_id": "montgomery",
                "start_ms": 35900,
                "end_ms": 44800,
                "text_en": "Can sophisticated adversaries bypass sequence homology filters through adversarial amino acid substitutions that alter sequence composition while preserving functional toxicity?",
                "text_tr": "Gelişmiş düşmanlar fonksiyonel toksisiteyi korurken dizi bileşimini değiştiren düşmanca amino asit ikameleri yoluyla dizi homolojisi filtrelerini atlatabilir mi?"
            },
            {
                "index": 6,
                "speaker_id": "nihal",
                "start_ms": 45200,
                "end_ms": 55000,
                "text_en": "That is why we enforce post-computation structural screening: we simulate 3D tertiary protein folds and molecular dynamics binding before releasing output coordinates. High-risk toxicity signatures trigger automated red-alerts and cryptographic lockouts.",
                "text_tr": "Bu yüzden hesaplama sonrası yapısal tarama uyguluyoruz: Çıktı koordinatlarını yayınlamadan önce 3D üçüncül protein kıvrımlarını ve moleküler dinamik bağlanmasını simüle ediyoruz. Yüksek riskli toksisite imzaları otomatik kırmızı alarmları ve kriptografik kilitlemeleri tetikler."
            }
        ],
        "key_vocabulary": [
            {
                "word": "adjudicate",
                "vocab_id": "vocab.adjudicate",
                "context_note_tr": "Biyoetik kurullarında çift kullanımlı araştırmaların ve güvenlik risklerinin hükme bağlanması."
            },
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Moleküler dinamik ve afinite sınıflandırıcılarıyla toksisite riskini somutlaştırmak."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c2_bio_01",
                "What dual-use capability creates severe biosecurity risks in the generative molecular diffusion platform?",
                "Üretken moleküler difüzyon platformunda hangi çift kullanımlı yetenek ciddi biyogüvenlik riskleri yaratmaktadır?",
                "The same models that design cancer antibodies can theoretically generate lethal toxins or immune-evasive pathogens",
                [
                    "The model can accidentally order too many test tubes from chemistry supply stores",
                    "The software requires high-resolution laboratory photography",
                    "The algorithms can translate medical books into foreign languages without permission"
                ],
                "Nihal acknowledges that the same generative priors that discover antibodies could design toxins and pathogens.",
                "Nihal antikor keşfeden modelin teorik olarak ölümcül toksinler ve patojenler de tasarlayabileceğini belirtir."
            ),
            build_q(
                "q_c2_bio_02",
                "What initial screening check is applied to incoming molecular design prompts?",
                "Gelen moleküler tasarım komutlarına hangi ilk tarama kontrolü uygulanmaktadır?",
                "Parsing prompts against select agent DNA databases using structural homology mapping and affinity classifiers",
                [
                    "Checking whether the user typed their email address using capital letters",
                    "Requiring users to upload a copy of their high school biology diploma",
                    "Sending prompts to an online public poll for voting approval"
                ],
                "Nihal explains prompts are parsed against select agent databases using homology and binding classifiers.",
                "Nihal komutların homoloji haritalaması ve afinite sınıflandırıcılarıyla ajan veritabanlarına karşı tarandığını açıklar."
            ),
            build_q(
                "q_c2_bio_03",
                "How could adversaries potentially bypass naive linear sequence filters?",
                "Saldırganlar naif doğrusal dizi filtrelerini potansiyel olarak nasıl atlatabilir?",
                "Through adversarial amino acid substitutions that alter sequence composition while preserving 3D functional toxicity",
                [
                    "By typing prompts in reverse alphabetical order",
                    "By printing prompts on paper and scanning them with a fax machine",
                    "By submitting queries only between midnight and two a.m."
                ],
                "Montgomery asks if adversaries could use amino acid substitutions to alter sequence while retaining toxicity.",
                "Montgomery amino asit ikameleriyle diziyi değiştirirken toksisiteyi koruma riskine dikkat çeker."
            ),
            build_q(
                "q_c2_bio_04",
                "What advanced post-computation technique defeats adversarial sequence disguise?",
                "Düşmanca dizi kılık değiştirmesini hangi gelişmiş hesaplama sonrası teknik engellemektedir?",
                "Simulating 3D tertiary protein folds and molecular dynamics binding before releasing output coordinates",
                [
                    "Erasing the entire artificial intelligence model from all computers",
                    "Hiring fifty police officers to guard the laboratory front entrance",
                    "Turning off the internet access for all medical doctors"
                ],
                "Nihal explains post-computation structural screening simulates 3D tertiary folds and binding dynamics.",
                "Nihal hesaplama sonrası 3D protein kıvrımı ve moleküler dinamik simülasyonu yapıldığını açıklar."
            ),
            build_q(
                "q_c2_bio_05",
                "What automated response occurs when high-risk toxicity signatures are detected?",
                "Yüksek riskli toksisite imzaları tespit edildiğinde hangi otomatik yanıt gerçekleşir?",
                "Automated red-alerts and cryptographic lockouts are immediately triggered",
                [
                    "The user is awarded five thousand bonus points on the platform",
                    "The laboratory computers physically shut down and delete their operating systems",
                    "A polite apology email is sent to the user with a discount coupon"
                ],
                "Nihal concludes that high-risk signatures trigger automated red-alerts and cryptographic lockouts.",
                "Nihal yüksek riskli imzaların otomatik kırmızı alarmları ve kriptografik kilitlemeyi tetiklediğini açıklar."
            )
        ],
        "topic_tags": ["bioethics", "generative-biology", "dual-use-biosecurity", "ai-governance", "c2-mastery"],
        "related_ids": ["vocab.adjudicate", "vocab.substantiate"],
        "status": "APPROVED",
        "version": 1
    }
]

if __name__ == "__main__":
    print(f"Generated {len(C2_LISTENING_SCENARIOS)} C2 listening scenarios.")
    for s in C2_LISTENING_SCENARIOS:
        print(f"  [{s['cefr_level']}] {s['id']} - {s['title']} ({len(s['transcript_items'])} items, {len(s['comprehension_questions'])} questions)")
