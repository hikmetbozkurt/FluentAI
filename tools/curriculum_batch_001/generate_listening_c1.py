#!/usr/bin/env python3
"""
Listening Generator for C1 (10 new scenarios, bringing C1 total to 11).
All scenarios adhere to listening.schema.json:
- CEFR: C1
- Valid category enum
- Real speakers
- Timestamped transcript items with text_en and text_tr
- 5 MCQs per scenario using McqBalancer
- Key vocabulary and related_ids with verified IDs
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=604)

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

C1_LISTENING_SCENARIOS = [
    {
        "id": "listening.c1.zero-trust-network-architecture",
        "title": "Security Architecture: Transitioning to Zero-Trust Micro-Segmentation",
        "cefr_level": "C1",
        "category": "engineering_meeting",
        "scenario_context": "Tolga, principal security architect, and Vivienne, VP of Network Infrastructure, deliberate replacing legacy corporate VPN perimeters with identity-aware zero-trust proxies and mutual TLS.",
        "speakers": [
            {"id": "tolga", "name": "Tolga", "role": "Principal Security Architect", "accent": "Turkish"},
            {"id": "vivienne", "name": "Vivienne", "role": "VP of Network Infrastructure", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c1_zero_trust.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "vivienne",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Tolga, our perimeter firewall model is increasingly untenable now that eighty percent of our engineering workforce operates in a decentralized hybrid arrangement.",
                "text_tr": "Tolga, mühendislik iş gücümüzün yüzde sekseni merkezi olmayan hibrit bir düzende çalışırken, çevre güvenlik duvarı modelimiz giderek savunulamaz hale geliyor."
            },
            {
                "index": 2,
                "speaker_id": "tolga",
                "start_ms": 7600,
                "end_ms": 16500,
                "text_en": "Indeed Vivienne. The fundamental flaw of castle-and-moat perimeter security is implicit lateral trust: once an attacker compromises an edge VPN endpoint, they can traverse our internal subnet unhindered.",
                "text_tr": "Kesinlikle Vivienne. Kale-ve-hendek çevre güvenliğinin temel kusuru örtük yanal güvendir: Bir saldırgan uçtaki bir VPN noktasını ele geçirdiğinde, dahili alt ağımızda engelsizce ilerleyebilir."
            },
            {
                "index": 3,
                "speaker_id": "vivienne",
                "start_ms": 16900,
                "end_ms": 24800,
                "text_en": "So you are advocating a complete Zero-Trust transition. How do you propose enforcing continuous cryptographic verification without introducing crippling latency?",
                "text_tr": "Yani tamamen Sıfır Güven (Zero-Trust) geçişini savunuyorsun. Felç edici bir gecikme yaratmadan sürekli kriptografik doğrulamayı nasıl uygulamayı öneriyorsun?"
            },
            {
                "index": 4,
                "speaker_id": "tolga",
                "start_ms": 25200,
                "end_ms": 34800,
                "text_en": "We will implement an Envoy-based service mesh with SPIFFE-compliant ephemeral X.509 certificates, enforcing mutual TLS on every East-West microservice communication hop.",
                "text_tr": "Her Doğu-Batı mikro servis iletişim sekmesinde karşılıklı TLS'i (mTLS) zorunlu kılan, SPIFFE uyumlu geçici X.509 sertifikalarına sahip Envoy tabanlı bir servis ağı uygulayacağız."
            },
            {
                "index": 5,
                "speaker_id": "vivienne",
                "start_ms": 35200,
                "end_ms": 44500,
                "text_en": "What about legacy monolithic workloads running on bare-metal database instances that cannot embed sidecar proxy containers?",
                "text_tr": "Peki yan araç (sidecar) vekil konteynerleri barındıramayan çıplak metal (bare-metal) veritabanı örneklerinde çalışan eski monolitik iş yükleri ne olacak?"
            },
            {
                "index": 6,
                "speaker_id": "tolga",
                "start_ms": 44900,
                "end_ms": 54000,
                "text_en": "We will place them behind identity-aware ingress gateways with strict software-defined eBPF kernel packet filtering, verifying device posture and OAuth claims before opening single-packet sockets.",
                "text_tr": "Onları tek paketlik soketleri açmadan önce cihaz güvenliğini ve OAuth taleplerini doğrulayan, katı yazılım tanımlı eBPF çekirdek paket filtrelemeli kimlik duyarlı ağ geçitlerinin ardına yerleştireceğiz."
            }
        ],
        "key_vocabulary": [
            {
                "word": "vulnerability",
                "vocab_id": "vocab.vulnerability",
                "context_note_tr": "Ağ çevrelerinde örtük güven kaynaklı yanal sızma zafiyetleri."
            },
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "Sıfır güven doğrulamalarında servis ağı proxy gecikmesi."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_zt_01",
                "What structural vulnerability of perimeter VPN security does Tolga highlight?",
                "Tolga çevre VPN güvenliğinin hangi yapısal zafiyetini vurgulamaktadır?",
                "Implicit lateral trust that allows attackers to traverse internal subnets once a perimeter endpoint is breached",
                [
                    "VPN encryption consumes ninety percent of employee home electricity",
                    "Perimeter firewalls only permit network connections during daytime hours",
                    "VPN technology was banned by international United Nations treaties"
                ],
                "Tolga points out that castle-and-moat security grants implicit trust, allowing attackers to move laterally unhindered.",
                "Tolga kale-ve-hendek güvenliğinin örtük güven tanıdığını ve sızan bir saldırganın ağ içinde serbestçe ilerleyebildiğini söyler."
            ),
            build_q(
                "q_c1_zt_02",
                "How will mutual TLS (mTLS) be orchestrated across the microservice mesh?",
                "Mikro servis ağı genelinde karşılıklı TLS (mTLS) nasıl orkestre edilecektir?",
                "Through an Envoy service mesh with SPIFFE-compliant ephemeral X.509 certificates",
                [
                    "By typing a secret administrative password manually into every server terminal each morning",
                    "By requiring all network data to be transmitted over public broadcast radio",
                    "By replacing all computer processors with analog mechanical gears"
                ],
                "Tolga specifies implementing an Envoy service mesh using SPIFFE-compliant ephemeral X.509 certificates.",
                "Tolga SPIFFE uyumlu geçici X.509 sertifikalı Envoy servis ağı kuracaklarını belirtir."
            ),
            build_q(
                "q_c1_zt_03",
                "How will bare-metal legacy databases without sidecar containers be protected?",
                "Yan araç konteynerleri barındıramayan çıplak metal eski veritabanları nasıl korunacaktır?",
                "Behind identity-aware gateways enforcing eBPF kernel packet filtering and posture verification",
                [
                    "By disconnecting them physically from power whenever an engineer logs off",
                    "By moving all physical servers into an underground concrete vault",
                    "By publishing all database records openly on social media"
                ],
                "Tolga explains they will place them behind identity-aware ingress gateways with eBPF filtering.",
                "Tolga onları eBPF filtrelemeli kimlik duyarlı ağ geçitlerinin ardına yerleştireceklerini ifade eder."
            ),
            build_q(
                "q_c1_zt_04",
                "What percentage of the workforce currently operates in a decentralized hybrid arrangement?",
                "İş gücünün şu anda yüzde kaçı merkezi olmayan hibrit bir düzende çalışmaktadır?",
                "Eighty percent",
                [
                    "Ten percent",
                    "One hundred percent completely remote",
                    "Exactly five percent"
                ],
                "Vivienne notes: 'eighty percent of our engineering workforce operates in a decentralized hybrid arrangement.'",
                "Vivienne personelin %80'inin hibrit çalıştığını belirtir."
            ),
            build_q(
                "q_c1_zt_05",
                "What verification checks must succeed before the gateway opens single-packet sockets to legacy nodes?",
                "Ağ geçidi eski düğümlere paket açmadan önce hangi doğrulama kontrolleri başarılı olmalıdır?",
                "Device posture verification and valid OAuth claims",
                [
                    "Employee fingerprints submitted through physical postal mail",
                    "Unanimous executive board voting approval in person",
                    "A complete factory reset of the employee's personal smartphone"
                ],
                "Tolga explains that the gateway verifies device posture and OAuth claims prior to opening sockets.",
                "Tolga soket açılmadan önce cihaz güvenliği ve OAuth taleplerinin doğrulanacağını söyler."
            )
        ],
        "topic_tags": ["zero-trust", "cybersecurity", "mtls", "service-mesh", "c1-mastery"],
        "related_ids": ["vocab.vulnerability", "vocab.latency"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.distributed-tracing-observability",
        "title": "Observability Strategy: Mitigating High-Cardinality Telemetry Overhead",
        "cefr_level": "C1",
        "category": "engineering_meeting",
        "scenario_context": "Elif, principal observability engineer, and Henrik, chief platform architect, analyze OpenTelemetry head-based vs tail-based sampling to mitigate storage costs and network overhead.",
        "speakers": [
            {"id": "elif", "name": "Elif", "role": "Principal Observability Engineer", "accent": "Turkish"},
            {"id": "henrik", "name": "Henrik", "role": "Chief Platform Architect", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c1_observability_overhead.mp3",
        "duration_seconds": 54,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "henrik",
                "start_ms": 0,
                "end_ms": 6800,
                "text_en": "Elif, our OpenTelemetry ingestion pipelines are consuming four petabytes of storage monthly. We are experiencing catastrophic cardinality explosion.",
                "text_tr": "Elif, OpenTelemetry veri alım hatlarımız ayda dört petabayt depolama tüketiyor. Felaket boyutunda bir kardinalite patlaması (cardinality explosion) yaşıyoruz."
            },
            {
                "index": 2,
                "speaker_id": "elif",
                "start_ms": 7200,
                "end_ms": 16200,
                "text_en": "I audited our metrics collectors, Henrik. Development teams have been embedding dynamic user UUIDs and IP addresses into Prometheus metric labels, multiplying unique time-series exponentially.",
                "text_tr": "Metrik toplayıcılarımızı denetledim Henrik. Geliştirme ekipleri Prometheus metrik etiketlerine dinamik kullanıcı UUID'lerini ve IP adreslerini gömmüş, bu da benzersiz zaman serilerini katlanarak çoğaltmış."
            },
            {
                "index": 3,
                "speaker_id": "henrik",
                "start_ms": 16600,
                "end_ms": 24500,
                "text_en": "That completely violates metric modeling best practices. High-cardinality attributes belong in structured traces and log spans, never in metric labels.",
                "text_tr": "Bu durum metrik modelleme en iyi uygulamalarını tamamen ihlal ediyor. Yüksek kardinaliteli öznitelikler yapılandırılmış izler (trace) ve log aralıklarına aittir, asla metrik etiketlerine değil."
            },
            {
                "index": 4,
                "speaker_id": "elif",
                "start_ms": 24900,
                "end_ms": 33800,
                "text_en": "Beyond stripping those tags, we need to transition our distributed tracing from naive head-based sampling to intelligent tail-based sampling in the collector layer.",
                "text_tr": "Bu etiketleri temizlemenin ötesinde dağıtık izleme sistemimizi toplayıcı katmanında naif baştan örneklemeden (head-based) akıllı sondan örneklemeye (tail-based) geçirmeliyiz."
            },
            {
                "index": 5,
                "speaker_id": "henrik",
                "start_ms": 34200,
                "end_ms": 43000,
                "text_en": "Explain how tail-based sampling protects our forensic debugging capabilities without retaining ninety-nine percent of boring healthy requests.",
                "text_tr": "Sondan örneklemenin sıkıcı sağlıklı isteklerin yüzde doksan dokuzunu saklamadan adli hata ayıklama yeteneklerimizi nasıl koruduğunu açıklar mısın?"
            },
            {
                "index": 6,
                "speaker_id": "elif",
                "start_ms": 43400,
                "end_ms": 53000,
                "text_en": "The collector buffers entire trace trees in memory until the root span concludes. It samples one hundred percent of traces containing HTTP 5xx errors or latency exceeding two seconds, while discarding benign fast traces.",
                "text_tr": "Toplayıcı kök aralık tamamlanana kadar tüm iz ağaçlarını bellekte tamponlar. HTTP 5xx hataları içeren veya iki saniyeyi aşan gecikmeye sahip izlerin yüzde yüzünü örneklerken zararsız hızlı izleri eler."
            }
        ],
        "key_vocabulary": [
            {
                "word": "audit",
                "vocab_id": "vocab.audit",
                "context_note_tr": "Telemetri metrikleri ve etiket kardinalitesinin sistemik denetimi."
            },
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "İzleme sistemlerinde yüksek gecikmeli uç durumların yakalanması."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_obs_01",
                "What developer practice caused the cardinality explosion in Prometheus metrics?",
                "Prometheus metriklerinde kardinalite patlamasına hangi geliştirici uygulaması yol açtı?",
                "Embedding dynamic high-cardinality attributes like user UUIDs and IP addresses into metric labels",
                [
                    "Running software servers in different geographic time zones",
                    "Writing code comments entirely in uppercase characters",
                    "Testing software on mobile tablets instead of desktop computers"
                ],
                "Elif explains that developers embedded dynamic user UUIDs and IPs into metric labels, causing time-series explosion.",
                "Elif geliştiricilerin dinamik kullanıcı kimlikleri ve IP'leri etiketlere gömerek zaman serisi patlaması yarattığını açıklar."
            ),
            build_q(
                "q_c1_obs_02",
                "How much telemetry storage is currently consumed on a monthly basis?",
                "Şu anda aylık bazda ne kadar telemetri depolama alanı tüketilmektedir?",
                "Four petabytes monthly",
                [
                    "Fifty megabytes",
                    "Ten gigabytes",
                    "One hundred kilobytes"
                ],
                "Henrik states: 'our OpenTelemetry ingestion pipelines are consuming four petabytes of storage monthly.'",
                "Henrik hatların ayda dört petabayt tükettiğini belirtir."
            ),
            build_q(
                "q_c1_obs_03",
                "Where do high-cardinality attributes legitimately belong according to Henrik?",
                "Henrik'e göre yüksek kardinaliteli öznitelikler haklı olarak nereye aittir?",
                "In structured traces and log spans, never in metric labels",
                [
                    "In physical handwritten legal notebooks",
                    "Printed on paper and stored in office filing cabinets",
                    "Posted publicly on company social media pages"
                ],
                "Henrik stresses: 'High-cardinality attributes belong in structured traces and log spans, never in metric labels.'",
                "Henrik bu özniteliklerin etiketlere değil, iz ve log aralıklarına ait olduğunu vurgular."
            ),
            build_q(
                "q_c1_obs_04",
                "How does intelligent tail-based sampling operate in the collector layer?",
                "Akıllı sondan örnekleme (tail-based sampling) toplayıcı katmanında nasıl çalışır?",
                "It buffers entire trace trees in memory and retains all error-ridden or slow traces while discarding healthy traces",
                [
                    "It randomly deletes half of all incoming network requests before they reach the server",
                    "It charges users one dollar every time an error is reported",
                    "It records only requests sent between midnight and one a.m."
                ],
                "Elif explains that the collector buffers traces and samples 100% of errors and slow requests while dropping benign ones.",
                "Elif toplayıcının hatalı veya yavaş istekleri %100 saklayıp sağlıklıları ayıkladığını açıklar."
            ),
            build_q(
                "q_c1_obs_05",
                "What latency threshold triggers automatic retention in the proposed tail-based sampling filter?",
                "Önerilen sondan örnekleme filtresinde otomatik saklamayı hangi gecikme eşiği tetikler?",
                "Latency exceeding two seconds",
                [
                    "Latency exceeding one millisecond",
                    "Latency exceeding fifteen minutes",
                    "Latency exceeding one hour"
                ],
                "Elif notes it samples 100% of traces containing HTTP 5xx errors or latency exceeding two seconds.",
                "Elif 5xx hataları veya 2 saniyeyi aşan gecikmelerin %100 örneklendiğini belirtir."
            )
        ],
        "topic_tags": ["observability", "opentelemetry", "distributed-tracing", "prometheus", "c1-mastery"],
        "related_ids": ["vocab.audit", "vocab.latency"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.board-cybersecurity-crisis-briefing",
        "title": "Executive Briefing: Board Audit Committee Supply-Chain Breach Assessment",
        "cefr_level": "C1",
        "category": "executive_briefing",
        "scenario_context": "Baris, Chief Information Security Officer, briefs Diane, chair of the board audit committee, on an Advanced Persistent Threat supply-chain compromise and regulatory disclosure timelines.",
        "speakers": [
            {"id": "baris", "name": "Baris", "role": "Chief Information Security Officer", "accent": "Turkish"},
            {"id": "diane", "name": "Diane", "role": "Board Audit Committee Chair", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c1_board_cybersecurity.mp3",
        "duration_seconds": 56,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "diane",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Baris, thank you for convening this emergency briefing. The audit committee requires an unvarnished assessment of the supply-chain compromise reported this morning.",
                "text_tr": "Baris, bu acil bilgilendirme toplantısını düzenlediğin için teşekkürler. Denetim komitesi bu sabah bildirilen tedarik zinciri ihlaline ilişkin yalın ve net bir değerlendirme talep ediyor."
            },
            {
                "index": 2,
                "speaker_id": "baris",
                "start_ms": 7600,
                "end_ms": 17200,
                "text_en": "Understood, Diane. A sophisticated nation-state threat actor compromised the build infrastructure of our third-party code-signing utility, injecting a backdoored dependency into our CI pipeline.",
                "text_tr": "Anlaşıldı Diane. Gelişmiş bir ulus-devlet tehdit aktörü üçüncü taraf kod imzalama aracımızın derleme altyapısını ele geçirerek CI hattımıza arka kapı içeren bir bağımlılık enjekte etti."
            },
            {
                "index": 3,
                "speaker_id": "diane",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "What is the substantiated blast radius? Did the adversary achieve persistence inside production customer database clusters?",
                "text_tr": "Somutlaştırılmış etki alanı (blast radius) nedir? Düşman taraf canlı müşteri veritabanı kümeleri içinde kalıcılık sağlayabildi mi?"
            },
            {
                "index": 4,
                "speaker_id": "baris",
                "start_ms": 26200,
                "end_ms": 36000,
                "text_en": "Forensic telemetry confirms the payload was quarantined in the build sandbox. Our immutable ephemeral build runners terminated before the malware could establish command-and-control egress.",
                "text_tr": "Adli bilişim telemetrisi zararlı yükün derleme korumalı alanında (sandbox) karantinaya alındığını doğruluyor. Değişmez geçici derleme ortamlarımız zararlı yazılım komuta-kontrol çıkışı kuramadan sonlandı."
            },
            {
                "index": 5,
                "speaker_id": "diane",
                "start_ms": 36400,
                "end_ms": 45800,
                "text_en": "That is immensely reassuring from an operational continuity perspective. However, what are our statutory disclosure obligations under SEC and GDPR frameworks?",
                "text_tr": "Bu operasyonel süreklilik açısından son derece rahatlatıcı. Ancak SEC ve GDPR düzenlemeleri kapsamındaki yasal bildirim yükümlülüklerimiz nelerdir?"
            },
            {
                "index": 6,
                "speaker_id": "baris",
                "start_ms": 46200,
                "end_ms": 55500,
                "text_en": "Because zero customer PII was exfiltrated and operational impact was contained, mandatory four-day SEC material disclosure is not triggered. However, we will publish a proactive security advisory tomorrow.",
                "text_tr": "Hiçbir müşteri kişisel verisi dışarı sızdırılmadığı ve operasyonel etki sınırlandırıldığı için SEC'in zorunlu dört günlük maddi olay bildirimi tetiklenmedi. Ancak yarın proaktif bir güvenlik duyurusu yayımlayacağız."
            }
        ],
        "key_vocabulary": [
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Adli bilişim ve telemetri kanıtlarıyla saldırı boyutunu somutlaştırmak."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Tedarik zinciri saldırılarına karşı değişmez derleme ortamı dayanıklılığı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_brd_01",
                "What was the specific vector of the cybersecurity compromise?",
                "Siber güvenlik ihlalinin spesifik saldırı vektörü neydi?",
                "A supply-chain compromise of a third-party code-signing utility injecting a backdoored dependency",
                [
                    "A physical break-in by burglars through the executive boardroom windows",
                    "A lost USB thumb drive discovered in a public municipal park",
                    "An employee accidentally emailing passwords to all corporate customers"
                ],
                "Baris explains that threat actors compromised a third-party code-signing utility's build infrastructure.",
                "Baris üçüncü taraf kod imzalama aracının altyapısının ele geçirilip arka kapılı bağımlılık enjekte edildiğini açıklar."
            ),
            build_q(
                "q_c1_brd_02",
                "Why was the malicious payload unable to exfiltrate data or establish persistence?",
                "Zararlı yazılım yükü neden veri sızdıramadı veya sistemde kalıcılık sağlayamadı?",
                "It was quarantined in the build sandbox, and immutable ephemeral build runners terminated in time",
                [
                    "The hackers forgot to turn on their computer servers",
                    "The malware was written in a language that the operating system could not understand",
                    "Corporate legal lawyers threatened the hackers with lawsuits"
                ],
                "Baris confirms the payload was quarantined and ephemeral runners terminated before command-and-control egress.",
                "Baris geçici derleme ortamlarının komuta-kontrol bağlantısı kurulmadan sonlandığını doğrular."
            ),
            build_q(
                "q_c1_brd_03",
                "Did the adversary penetrate production customer database clusters?",
                "Saldırgan canlı müşteri veritabanı kümelerine sızabildi mi?",
                "No, forensic telemetry confirms the incident was entirely contained in the build sandbox",
                [
                    "Yes, all customer passwords and credit card numbers were stolen",
                    "Yes, but only databases located in South America",
                    "The forensic team was unable to determine what happened"
                ],
                "Baris confirms zero customer PII was exfiltrated and impact was contained in the sandbox.",
                "Baris hiçbir müşteri verisinin sızdırılmadığını ve olayın sandbox'ta sınırlandığını teyit eder."
            ),
            build_q(
                "q_c1_brd_04",
                "Why is the mandatory four-day SEC material disclosure rule NOT triggered?",
                "SEC'in zorunlu dört günlük maddi olay bildirimi kuralı neden tetiklenmemiştir?",
                "Because zero customer PII was compromised and there was no material operational impact",
                [
                    "Because the SEC does not have authority over technology corporations",
                    "Because the company paid a confidential fine to government officials",
                    "Because the incident took place on a weekend"
                ],
                "Baris states that because zero PII was exfiltrated and operational impact was contained, mandatory SEC disclosure is not triggered.",
                "Baris veri sızdırılmadığı ve maddi etki oluşmadığı için zorunlu SEC bildiriminin tetiklenmediğini belirtir."
            ),
            build_q(
                "q_c1_brd_05",
                "What transparent action will the company take tomorrow?",
                "Şirket yarın şeffaflık adına hangi adımı atacaktır?",
                "Publishing a proactive security advisory explaining the contained incident",
                [
                    "Firing the entire software development engineering division",
                    "Shutting down all company operations and filing for bankruptcy",
                    "Denying that any software tools were ever evaluated"
                ],
                "Baris concludes: 'we will publish a proactive security advisory tomorrow.'",
                "Baris yarın proaktif bir güvenlik duyurusu yayımlayacaklarını açıklar."
            )
        ],
        "topic_tags": ["cybersecurity", "board-governance", "supply-chain-attack", "incident-disclosure", "c1-mastery"],
        "related_ids": ["vocab.substantiate", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.multi-cloud-egress-cost-arbitrage",
        "title": "Infrastructure Negotiation: Cloud Egress Tariffs and Direct Peering",
        "cefr_level": "C1",
        "category": "negotiation",
        "scenario_context": "Selcuk, head of global infrastructure, negotiates dedicated fiber interconnect pricing with Gregory, cloud strategic alliances director, to eliminate exorbitant data egress fees.",
        "speakers": [
            {"id": "selcuk", "name": "Selcuk", "role": "Head of Global Infrastructure", "accent": "Turkish"},
            {"id": "gregory", "name": "Gregory", "role": "Cloud Alliances Director", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c1_egress_arbitrage.mp3",
        "duration_seconds": 53,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "selcuk",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Gregory, our multi-cloud architecture routes twelve petabytes of cross-cloud analytical replication each month. Your public internet egress tariff of eight cents per gigabyte is untenable.",
                "text_tr": "Gregory, çoklu bulut mimarimiz her ay on iki petabayt bulutlar arası analitik çoğaltma yönlendiriyor. Gigabayt başına sekiz sentlik genel internet çıkış tarifeniz savunulamaz."
            },
            {
                "index": 2,
                "speaker_id": "gregory",
                "start_ms": 7600,
                "end_ms": 16200,
                "text_en": "Selcuk, our standard public egress pricing reflects the transit backbone transit costs across Tier-1 carriers. However, we value your multi-million-dollar annual enterprise commitment.",
                "text_tr": "Selcuk, standart genel çıkış fiyatlandırmamız 1. Kademe taşıyıcılar genelindeki transit omurga maliyetlerini yansıtmaktadır. Ancak yıllık milyonlarca dolarlık kurumsal taahhüdünüze değer veriyoruz."
            },
            {
                "index": 3,
                "speaker_id": "selcuk",
                "start_ms": 16600,
                "end_ms": 25500,
                "text_en": "We are prepared to establish dedicated one-hundred-gigabit Direct Connect and Cloud Interconnect circuits in Equinix Frankfurt and Ashburn co-location facilities.",
                "text_tr": "Equinix Frankfurt ve Ashburn ortak yerleşim (co-location) tesislerinde özel yüz gigabitlik Direct Connect ve Cloud Interconnect devreleri kurmaya hazırız."
            },
            {
                "index": 4,
                "speaker_id": "gregory",
                "start_ms": 25900,
                "end_ms": 34800,
                "text_en": "If you terminate private interconnect circuits in those strategic metro exchange fabrics, we can reclassify your traffic from public egress to private interconnect data transfer.",
                "text_tr": "Bu stratejik metro değişim yapılarında özel ara bağlantı devrelerini sonlandırırsanız, trafiğinizi genel çıkıştan özel ara bağlantı veri transferi olarak yeniden sınıflandırabiliriz."
            },
            {
                "index": 5,
                "speaker_id": "selcuk",
                "start_ms": 35200,
                "end_ms": 43500,
                "text_en": "What exact unit rate can your alliances executive committee approve for sustained monthly volumes exceeding ten petabytes?",
                "text_tr": "İttifaklar icra komiteniz aylık on petabaytı aşan sürekli hacimler için tam olarak hangi birim fiyatı onaylayabilir?"
            },
            {
                "index": 6,
                "speaker_id": "gregory",
                "start_ms": 43900,
                "end_ms": 52500,
                "text_en": "We can compress the egress rate from eight cents down to one point two cents per gigabyte, provided you sign a thirty-six-month minimum commit addendum.",
                "text_tr": "Otuz altı aylık asgari taahhüt zeyilnamesini imzalamanız koşuluyla, çıkış ücretini sekiz sentten gigabayt başına bir virgül iki sente indirebiliriz."
            }
        ],
        "key_vocabulary": [
            {
                "word": "objective",
                "vocab_id": "vocab.objective",
                "context_note_tr": "Milyonlarca dolarlık bulut çıkış maliyetlerini düşürme stratejik hedefi."
            },
            {
                "word": "asynchronous",
                "vocab_id": "vocab.asynchronous",
                "context_note_tr": "Çoklu bulut veri ambarları arasındaki asenkron veri çoğaltma akışları."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_egr_01",
                "What monthly cross-cloud data replication volume is driving Selcuk's negotiation?",
                "Selcuk'un müzakeresini yönlendiren aylık bulutlar arası veri çoğaltma hacmi nedir?",
                "Twelve petabytes of data replication monthly",
                [
                    "One hundred megabytes",
                    "Fifty gigabytes",
                    "Five terabytes"
                ],
                "Selcuk opens by citing twelve petabytes of cross-cloud analytical replication monthly.",
                "Selcuk aylık 12 petabaytlık bulutlar arası analitik veri çoğaltma hacmini belirtir."
            ),
            build_q(
                "q_c1_egr_02",
                "What was the cloud provider's standard public internet egress tariff?",
                "Bulut sağlayıcısının standart genel internet çıkış tarifesi ne kadardı?",
                "Eight cents per gigabyte",
                [
                    "One dollar per megabyte",
                    "Free of charge for all corporations",
                    "Ten dollars per kilobyte"
                ],
                "Selcuk references: 'Your public internet egress tariff of eight cents per gigabyte is untenable.'",
                "Selcuk gigabayt başına sekiz sentlik genel internet tarifesini belirtir."
            ),
            build_q(
                "q_c1_egr_03",
                "In which co-location data centers does Selcuk propose establishing private interconnects?",
                "Selcuk hangi ortak yerleşim veri merkezlerinde özel ara bağlantılar kurmayı önermektedir?",
                "Equinix Frankfurt and Ashburn facilities",
                [
                    "Public university computer laboratories in Tokyo",
                    "Commercial retail electronics stores in London",
                    "Local municipal library server closets in Sydney"
                ],
                "Selcuk proposes 100-Gbps circuits in Equinix Frankfurt and Ashburn co-location facilities.",
                "Selcuk Equinix Frankfurt ve Ashburn tesislerinde 100 Gbps devreler kurmayı önerir."
            ),
            build_q(
                "q_c1_egr_04",
                "To what discounted rate does Gregory agree to reduce the egress cost per gigabyte?",
                "Gregory çıkış maliyetini gigabayt başına hangi indirimli fiyata düşürmeyi kabul etmektedir?",
                "One point two cents per gigabyte",
                [
                    "Seven point nine cents per gigabyte",
                    "Zero cents permanently",
                    "Five cents per gigabyte"
                ],
                "Gregory confirms: 'compress the egress rate from eight cents down to one point two cents per gigabyte.'",
                "Gregory fiyatın sekiz sentten 1,2 sente indirileceğini teyit eder."
            ),
            build_q(
                "q_c1_egr_05",
                "What contractual commitment is required from Selcuk's company to unlock the discounted tariff?",
                "İndirimli tarifeden yararlanmak için Selcuk'un şirketinden hangi sözleşme taahhüdü talep edilmektedir?",
                "A thirty-six-month minimum commitment addendum",
                [
                    "A ten-year cash upfront prepayment of fifty million dollars",
                    "An agreement to purchase all office hardware exclusively from Gregory",
                    "Surrendering fifty percent of company equity shares to the cloud provider"
                ],
                "Gregory specifies: 'provided you sign a thirty-six-month minimum commit addendum.'",
                "Gregory 36 aylık asgari taahhüt zeyilnamesi imzalanması şartını koşar."
            )
        ],
        "topic_tags": ["multi-cloud", "networking", "finops", "direct-connect", "c1-mastery"],
        "related_ids": ["vocab.objective", "vocab.asynchronous"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.ai-model-inference-latency-optimization",
        "title": "Machine Learning Systems: LLM Inference Quantization and PagedAttention",
        "cefr_level": "C1",
        "category": "engineering_meeting",
        "scenario_context": "Zehra, lead ML systems engineer, and Alistair, principal research scientist, evaluate INT4 weight quantization, vLLM PagedAttention, and TensorRT compilation to scale real-time AI inference.",
        "speakers": [
            {"id": "zehra", "name": "Zehra", "role": "Lead ML Systems Engineer", "accent": "Turkish"},
            {"id": "alistair", "name": "Alistair", "role": "Principal AI Research Scientist", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c1_llm_inference.mp3",
        "duration_seconds": 54,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "alistair",
                "start_ms": 0,
                "end_ms": 6800,
                "text_en": "Zehra, our fine-tuned 70-billion parameter language model is suffering severe GPU memory fragmentation. Time-to-first-token latency is averaging 1.4 seconds.",
                "text_tr": "Zehra, ince ayarlı 70 milyar parametreli dil modelimiz ciddi GPU bellek parçalanması yaşıyor. İlk belirteç süresi (TTFT) gecikmesi ortalama 1,4 saniye."
            },
            {
                "index": 2,
                "speaker_id": "zehra",
                "start_ms": 7200,
                "end_ms": 16500,
                "text_en": "The primary bottleneck is the Key-Value cache memory allocation. Naive PyTorch memory pre-allocation suffers up to eighty percent virtual memory waste due to unpredictable generation lengths.",
                "text_tr": "Temel darboğaz Anahtar-Değer (KV) önbellek bellek tahsisidir. Naif PyTorch bellek ön tahsisi, öngörülemeyen üretim uzunlukları nedeniyle yüzde seksen sanal bellek israfı yaşıyor."
            },
            {
                "index": 3,
                "speaker_id": "alistair",
                "start_ms": 16900,
                "end_ms": 25200,
                "text_en": "Are you suggesting migrating our production serving runtime to vLLM using PagedAttention inspired by operating system virtual memory paging?",
                "text_tr": "Canlı sunum çalışma zamanımızı işletim sistemi sanal bellek sayfalamasından esinlenen PagedAttention kullanan vLLM'e taşımamızı mı öneriyorsun?"
            },
            {
                "index": 4,
                "speaker_id": "zehra",
                "start_ms": 25600,
                "end_ms": 34800,
                "text_en": "Yes. PagedAttention partitions the KV cache into non-contiguous physical memory blocks, virtually eliminating memory fragmentation and quadrupling batch concurrency throughput.",
                "text_tr": "Evet. PagedAttention, KV önbelleğini bitişik olmayan fiziksel bellek bloklarına bölerek bellek parçalanmasını neredeyse tamamen ortadan kaldırır ve yığın eşzamanlılık verimini dört katına çıkarır."
            },
            {
                "index": 5,
                "speaker_id": "alistair",
                "start_ms": 35200,
                "end_ms": 44000,
                "text_en": "What about model weight quantization? Can we adopt AWQ 4-bit activation-aware weight quantization without degrading our BLEU and MMLU benchmark accuracy?",
                "text_tr": "Peki ya model ağırlık kuantizasyonu? BLEU ve MMLU kıyaslama doğruluğumuzu düşürmeden AWQ 4-bit aktivasyon duyarlı ağırlık kuantizasyonunu benimseyebilir miyiz?"
            },
            {
                "index": 6,
                "speaker_id": "zehra",
                "start_ms": 44400,
                "end_ms": 53500,
                "text_en": "Benchmark evaluations show AWQ INT4 preserves ninety-nine point four percent of FP16 reasoning capability while halving VRAM requirements, enabling single-node dual-A100 deployment.",
                "text_tr": "Kıyaslama değerlendirmeleri AWQ INT4'ün FP16 akıl yürütme yeteneğinin yüzde doksan dokuz virgül dördünü korurken VRAM gereksinimlerini yarıya indirdiğini ve tek düğümlü çift A100 dağıtımına olanak tanıdığını gösteriyor."
            }
        ],
        "key_vocabulary": [
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "Büyük dil modellerinde ilk belirteç üretim süresi (TTFT) gecikmesi."
            },
            {
                "word": "bottleneck",
                "vocab_id": "vocab.bottleneck",
                "context_note_tr": "GPU bellek parçalanması ve KV önbellek darboğazları."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_llm_01",
                "What operational challenge is degrading the 70-billion parameter model's inference performance?",
                "70 milyar parametreli modelin çıkarım performansını hangi operasyonel sorun düşürmektedir?",
                "Severe GPU memory fragmentation causing time-to-first-token latency to average 1.4 seconds",
                [
                    "Computer monitors turning completely black during neural network compilation",
                    "A total failure of the company's external electrical generator",
                    "The model outputting text exclusively in ancient hieroglyphic symbols"
                ],
                "Alistair states that GPU memory fragmentation is causing time-to-first-token latency to hit 1.4 seconds.",
                "Alistair GPU bellek parçalanmasının TTFT gecikmesini 1,4 saniyeye çıkardığını belirtir."
            ),
            build_q(
                "q_c1_llm_02",
                "Why does naive PyTorch KV cache allocation waste up to eighty percent of memory?",
                "Naif PyTorch KV önbellek tahsisi neden yüzde seksene kadar bellek israfına yol açmaktadır?",
                "Unpredictable output token generation lengths require allocating worst-case static memory buffers",
                [
                    "PyTorch automatically downloads high-resolution video files in the background",
                    "Software developers write inefficient math loops that run for ten hours",
                    "GPU memory chips lose all electrical charge every fifteen seconds"
                ],
                "Zehra explains that naive pre-allocation suffers up to 80% virtual memory waste due to unpredictable lengths.",
                "Zehra değişken üretim uzunlukları nedeniyle statik tahsisin %80 bellek israfı yarattığını açıklar."
            ),
            build_q(
                "q_c1_llm_03",
                "What architectural innovation does PagedAttention introduce to eliminate fragmentation?",
                "PagedAttention bellek parçalanmasını ortadan kaldırmak için hangi mimari yeniliği getirmektedir?",
                "Partitioning the KV cache into non-contiguous physical memory blocks inspired by OS virtual paging",
                [
                    "Deleting all model weights after every individual word is generated",
                    "Storing model weights exclusively on physical magnetic cassette tapes",
                    "Transferring all computations to personal smartphone processors"
                ],
                "Zehra explains that PagedAttention partitions the cache into non-contiguous blocks, virtually eliminating fragmentation.",
                "Zehra önbelleği bitişik olmayan bloklara bölerek parçalanmanın önlendiğini belirtir."
            ),
            build_q(
                "q_c1_llm_04",
                "How much reasoning accuracy is preserved when quantizing to AWQ INT4?",
                "AWQ INT4 kuantizasyonuna geçildiğinde akıl yürütme doğruluğunun ne kadarı korunmaktadır?",
                "Ninety-nine point four percent of FP16 reasoning capability",
                [
                    "Only ten percent of original capability",
                    "Zero percent, resulting in gibberish text",
                    "Fifty percent accuracy loss"
                ],
                "Zehra notes benchmark evaluations show AWQ INT4 preserves 99.4% of FP16 capability.",
                "Zehra kıyaslama testlerinin FP16 yeteneğinin %99,4'ünün korunduğunu gösterdiğini açıklar."
            ),
            build_q(
                "q_c1_llm_05",
                "What hardware deployment efficiency does INT4 quantization unlock for this model?",
                "INT4 kuantizasyonu bu model için hangi donanım dağıtım verimliliğini açmaktadır?",
                "Enabling deployment on a single-node dual-A100 GPU server instead of expensive multi-node clusters",
                [
                    "Allowing the model to run on a digital wristwatch without a battery",
                    "Eliminating the need to use electrical power cables",
                    "Running the entire model inside an offline web browser cache"
                ],
                "Zehra concludes that halving VRAM requirements enables single-node dual-A100 deployment.",
                "Zehra VRAM ihtiyacının yarıya inmesiyle modelin tek düğümlü çift A100 üzerinde çalışabildiğini belirtir."
            )
        ],
        "topic_tags": ["machine-learning", "llm-inference", "pagedattention", "quantization", "c1-mastery"],
        "related_ids": ["vocab.latency", "vocab.bottleneck"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.algorithmic-trading-concurrency-race",
        "title": "Systems Post-Mortem: Ultra-Low-Latency Concurrency Race Condition",
        "cefr_level": "C1",
        "category": "incident_response",
        "scenario_context": "Emre, high-frequency trading systems architect, reviews an order book execution race condition with Sebastian, head of quantitative research, following erratic options volatility.",
        "speakers": [
            {"id": "emre", "name": "Emre", "role": "HFT Systems Architect", "accent": "Turkish"},
            {"id": "sebastian", "name": "Sebastian", "role": "Head of Quantitative Research", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c1_hft_race.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "sebastian",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Emre, during yesterday's market open volatility auction, our trading engine executed twenty-four duplicate limit orders, exceeding our risk exposure threshold by eighteen million dollars.",
                "text_tr": "Emre, dünkü piyasa açılış oynaklık müzayedesinde ticaret motorumuz yirmi dört mükerrer limit emri gerçekleştirdi ve risk maruziyeti eşiğimizi on sekiz milyon dolar aştı."
            },
            {
                "index": 2,
                "speaker_id": "emre",
                "start_ms": 7600,
                "end_ms": 16800,
                "text_en": "I isolated the root cause in our order book ring buffer. Under extreme market packet ingestion, two worker threads experienced an out-of-order memory visibility anomaly across CPU cache lines.",
                "text_tr": "Kök nedeni emir defteri dairesel tamponumuzda (ring buffer) izole ettim. Aşırı piyasa paketi alımı altında iki çalışan iş parçacığı CPU önbellek satırları arasında sıra dışı bellek görünürlüğü anomalisi yaşadı."
            },
            {
                "index": 3,
                "speaker_id": "sebastian",
                "start_ms": 17200,
                "end_ms": 25000,
                "text_en": "Were we relying on relaxed memory ordering semantics instead of strict acquire-release semantics in our lock-free concurrent queue?",
                "text_tr": "Kilitsiz eşzamanlı kuyruğumuzda katı 'acquire-release' anlambilimi yerine gevşek bellek sıralama anlambilimine mi güveniyorduk?"
            },
            {
                "index": 4,
                "speaker_id": "emre",
                "start_ms": 25400,
                "end_ms": 34800,
                "text_en": "Exactly. A recent micro-optimization used memory_order_relaxed on atomic sequence counters to shave four nanoseconds, allowing the compiler and CPU to reorder write flushes.",
                "text_tr": "Kesinlikle. Yakın zamanda yapılan bir mikro optimizasyon, dört nanosaniye kazanmak için atomik sıra sayaçlarında memory_order_relaxed kullandı ve bu da derleyicinin ve CPU'nun yazma boşaltmalarını yeniden sıralamasına izin verdi."
            },
            {
                "index": 5,
                "speaker_id": "sebastian",
                "start_ms": 35200,
                "end_ms": 44500,
                "text_en": "Four nanoseconds of latency optimization is meaningless if it jeopardizes deterministic risk state. What structural invariants are we establishing immediately?",
                "text_tr": "Deterministik risk durumunu tehlikeye atıyorsa dört nanosaniyelik gecikme optimizasyonu anlamsızdır. Derhal hangi yapısal değişmezleri tesis ediyoruz?"
            },
            {
                "index": 6,
                "speaker_id": "emre",
                "start_ms": 44900,
                "end_ms": 54000,
                "text_en": "We restored acquire-release barriers, pinned worker threads to isolated NUMA cores, and integrated ThreadSanitizer automated race detection into our pull-request validation pipelines.",
                "text_tr": "Acquire-release bariyerlerini geri yükledik, çalışan iş parçacıklarını yalıtılmış NUMA çekirdeklerine sabitledik ve PR doğrulama hatlarımıza ThreadSanitizer otomatik yarış algılamasını entegre ettik."
            }
        ],
        "key_vocabulary": [
            {
                "word": "latency",
                "vocab_id": "vocab.latency",
                "context_note_tr": "Yüksek frekanslı alım satım sistemlerinde nanosaniye düzeyinde işlem gecikmesi."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Kritik finansal sistemlerde eşzamanlılık yarışlarına karşı deterministik direnç."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_hft_01",
                "What financial and operational anomaly triggered this urgent systems post-mortem?",
                "Bu acil sistem sonrası incelemesini hangi finansal ve operasyonel anomali tetikledi?",
                "The engine executed twenty-four duplicate limit orders, exceeding risk thresholds by eighteen million dollars",
                [
                    "A bank robbery occurred at the company's physical headquarters",
                    "The stock exchange cancelled all equity trading for three consecutive months",
                    "Computer monitors were physically damaged by electrical short circuits"
                ],
                "Sebastian opens by reporting that 24 duplicate limit orders exceeded risk thresholds by $18 million.",
                "Sebastian 24 mükerrer emrin risk eşiğini 18 milyon dolar aştığını bildirir."
            ),
            build_q(
                "q_c1_hft_02",
                "What was the root cause of the duplicate order execution?",
                "Mükerrer emir yürütmenin kök nedeni neydi?",
                "Out-of-order memory visibility across CPU cache lines caused by relaxed memory ordering in a lock-free queue",
                [
                    "A mouse chewed through an Ethernet cable inside the server rack",
                    "A software engineer accidentally deleted the company's primary database",
                    "The system was flooded with millions of customer email complaints"
                ],
                "Emre explains that using memory_order_relaxed allowed compiler and CPU reordering across threads.",
                "Emre memory_order_relaxed kullanımının CPU ve derleyicinin yazmaları yeniden sıralamasına yol açtığını açıklar."
            ),
            build_q(
                "q_c1_hft_03",
                "Why had developers introduced the problematic relaxed memory ordering?",
                "Geliştiriciler sorunlu gevşek bellek sıralamasını neden uygulamıştı?",
                "To shave four nanoseconds of latency off atomic sequence counters",
                [
                    "To reduce electricity consumption by fifty percent",
                    "To comply with international tax reporting regulations",
                    "To make the code readable for introductory computer science students"
                ],
                "Emre notes a micro-optimization used relaxed ordering to save 4 nanoseconds.",
                "Emre 4 nanosaniye kazanmak amacıyla yapılan bir optimizasyonun buna sebep olduğunu belirtir."
            ),
            build_q(
                "q_c1_hft_04",
                "What CPU architectural pinning strategy did Emre implement to eliminate thread migration jitter?",
                "İş parçacığı geçiş titremesini ortadan kaldırmak için Emre hangi CPU sabitleme stratejisini uyguladı?",
                "Pinning worker threads to isolated NUMA cores",
                [
                    "Running all software processes on a single shared GPU thread",
                    "Allowing the operating system to migrate threads randomly every second",
                    "Turning off hyper-threading across all office employee laptops"
                ],
                "Emre explains: 'pinned worker threads to isolated NUMA cores.'",
                "Emre iş parçacıklarını yalıtılmış NUMA çekirdeklerine sabitlediklerini belirtir."
            ),
            build_q(
                "q_c1_hft_05",
                "Which automated tool was added to the pull-request pipeline to catch future concurrency bugs?",
                "Gelecekteki eşzamanlılık hatalarını yakalamak için PR hattına hangi otomatik araç eklendi?",
                "ThreadSanitizer automated race detection",
                [
                    "SpellCheck automated grammar validator",
                    "Photoshop automated image resizer",
                    "Calculator automated multiplication tester"
                ],
                "Emre confirms integrating ThreadSanitizer automated race detection into validation pipelines.",
                "Emre doğrulama hatlarına ThreadSanitizer entegre ettiklerini açıklar."
            )
        ],
        "topic_tags": ["concurrency", "systems-architecture", "high-frequency-trading", "low-latency", "c1-mastery"],
        "related_ids": ["vocab.latency", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.enterprise-data-mesh-governance",
        "title": "Data Architecture: Decentralized Data Mesh vs Monolithic Lakehouse",
        "cefr_level": "C1",
        "category": "stakeholder_alignment",
        "scenario_context": "Hande, enterprise data architect, and Oliver, VP of Analytics, deliberate moving from a centralized data warehouse to a domain-driven Data Mesh architecture.",
        "speakers": [
            {"id": "hande", "name": "Hande", "role": "Enterprise Data Architect", "accent": "Turkish"},
            {"id": "oliver", "name": "Oliver", "role": "VP of Data Analytics", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c1_data_mesh.mp3",
        "duration_seconds": 54,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "oliver",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Hande, our central data engineering team is completely overwhelmed. Business units face four-month lead times for simple analytical data mart pipelines.",
                "text_tr": "Hande, merkezi veri mühendisliği ekibimiz tamamen tıkanmış durumda. İş birimleri basit analitik veri ambarı hatları için dört aylık teslim süreleriyle karşılaşıyor."
            },
            {
                "index": 2,
                "speaker_id": "hande",
                "start_ms": 7600,
                "end_ms": 16500,
                "text_en": "The root pathology is architectural centralization, Oliver. We are funneling heterogeneous data from forty distinct domains into a single monolithic lakehouse team that lacks domain context.",
                "text_tr": "Kök patoloji mimari merkezileşmedir Oliver. Kırk farklı etki alanından gelen heterojen verileri, etki alanı bağlamından yoksun tek bir monolitik göl evi (lakehouse) ekibine yönlendiriyoruz."
            },
            {
                "index": 3,
                "speaker_id": "oliver",
                "start_ms": 16900,
                "end_ms": 25200,
                "text_en": "Are you proposing Zhamak Dehghani's Data Mesh paradigm? Will decentralizing data ownership to business units devolve into unmanageable data silos?",
                "text_tr": "Zhamak Dehghani'nin Veri Ağı (Data Mesh) paradigmasını mı öneriyorsun? Veri sahipliğini iş birimlerine devretmek yönetilemez veri silolarına mı yol açacak?"
            },
            {
                "index": 4,
                "speaker_id": "hande",
                "start_ms": 25600,
                "end_ms": 34800,
                "text_en": "Not if we couple domain data ownership with automated federated computational governance. Domains must treat datasets as discoverable, versioned products with explicit SLAs.",
                "text_tr": "Eğer etki alanı veri sahipliğini otomatik federe bilişimsel yönetişimle birleştirirsek hayır. Etki alanları veri setlerine açık SLA'lara sahip, keşfedilebilir, sürümlendirilmiş ürünler olarak yaklaşmalıdır."
            },
            {
                "index": 5,
                "speaker_id": "oliver",
                "start_ms": 35200,
                "end_ms": 44000,
                "text_en": "What self-serve data infrastructure platform must our central team build to empower business domains without burdening them with raw DevOps toil?",
                "text_tr": "Merkezi ekibimiz iş etki alanlarını ham DevOps zahmetiyle boğmadan yetkilendirmek için hangi self-servis veri altyapı platformunu inşa etmelidir?"
            },
            {
                "index": 6,
                "speaker_id": "hande",
                "start_ms": 44400,
                "end_ms": 53500,
                "text_en": "A unified platform providing automated schema registry enforcement, automated lineage tracing through OpenLineage, and zero-trust column-level encryption out of the box.",
                "text_tr": "Kutudan çıktığı haliyle otomatik şema kaydı denetimi, OpenLineage aracılığıyla otomatik soy takibi ve sıfır güven sütun düzeyinde şifreleme sağlayan birleşik bir platform."
            }
        ],
        "key_vocabulary": [
            {
                "word": "consensus",
                "vocab_id": "vocab.consensus",
                "context_note_tr": "Etki alanları arasında federatif veri yönetişimi ve mimari uzlaşı."
            },
            {
                "word": "bottleneck",
                "vocab_id": "vocab.bottleneck",
                "context_note_tr": "Merkezi veri ambarı ekiplerinde oluşan teslimat darboğazları."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_mesh_01",
                "What operational symptom indicates that the central data engineering team is overwhelmed?",
                "Merkezi veri mühendisliği ekibinin tıkandığını hangi operasyonel belirti göstermektedir?",
                "Business units face four-month lead times for simple analytical data pipelines",
                [
                    "All company laptops are experiencing hardware graphics card failure",
                    "Data analysts are legally prohibited from querying databases",
                    "The cost of paper printouts exceeded ten million dollars"
                ],
                "Oliver opens by noting business units face 4-month lead times for simple pipelines.",
                "Oliver iş birimlerinin basit veri hatları için 4 ay beklemek zorunda kaldığını belirtir."
            ),
            build_q(
                "q_c1_mesh_02",
                "What core architectural paradigm does Hande advocate adopting?",
                "Hande hangi temel mimari paradigmayı benimsemeyi savunmaktadır?",
                "Zhamak Dehghani's Data Mesh paradigm",
                [
                    "Relational Database Normalization Form 1",
                    "Single-Tenant Flat Text File storage",
                    "Manual Spreadsheet Synchronization"
                ],
                "Hande and Oliver discuss adopting Zhamak Dehghani's Data Mesh paradigm.",
                "Hande ve Oliver Zhamak Dehghani'nin Data Mesh paradigmasını tartışırlar."
            ),
            build_q(
                "q_c1_mesh_03",
                "How does Data Mesh prevent decentralized domains from turning into chaotic data silos?",
                "Data Mesh merkezi olmayan etki alanlarının kaotik veri silolarına dönüşmesini nasıl önler?",
                "By enforcing automated federated computational governance and treating data as discoverable products with SLAs",
                [
                    "By requiring every SQL query to be hand-signed by the chief executive",
                    "By deleting all data sets after thirty days automatically",
                    "By banning domain teams from speaking to software developers"
                ],
                "Hande specifies coupling domain ownership with automated federated governance and treating data as products.",
                "Hande etki alanı sahipliğini federe yönetişim ve veriyi ürün olarak ele alma yaklaşımıyla birleştirmeyi belirtir."
            ),
            build_q(
                "q_c1_mesh_04",
                "Which open-source standard does Hande recommend for automated data lineage tracking?",
                "Hande otomatik veri soy takibi için hangi açık kaynak standardını önermektedir?",
                "OpenLineage",
                [
                    "Git Commit Log",
                    "Microsoft Paint",
                    "Docker Hub"
                ],
                "Hande specifically mentions automated lineage tracing through OpenLineage.",
                "Hande açıkça OpenLineage aracılığıyla otomatik soy takibini önerir."
            ),
            build_q(
                "q_c1_mesh_05",
                "What fundamental shift in data perspective does the Data Mesh promote?",
                "Data Mesh veri bakış açısında hangi temel dönüşümü teşvik eder?",
                "Shifting from viewing data as an incidental exhaust to treating domain datasets as discoverable, versioned products",
                [
                    "Viewing data as confidential secrets that must never be read by human eyes",
                    "Treating all data as marketing advertising material",
                    "Converting all numbers into random alphabetical letters"
                ],
                "Data Mesh treats data as an architectural product provided by business domains with contracts and SLAs.",
                "Data Mesh veriyi etki alanları tarafından sözleşmeler ve SLA'larla sunulan bir mimari ürün olarak görür."
            )
        ],
        "topic_tags": ["data-mesh", "data-architecture", "governance", "distributed-systems", "c1-mastery"],
        "related_ids": ["vocab.consensus", "vocab.bottleneck"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.cross-border-data-sovereignty",
        "title": "Legal & Technical Review: Cross-Border Data Sovereignty and Schrems II",
        "cefr_level": "C1",
        "category": "executive_briefing",
        "scenario_context": "Nazli, Chief Technology Officer, and Charles, senior regulatory legal counsel, evaluate international data transfer compliance, sovereign key escrow, and GDPR requirements.",
        "speakers": [
            {"id": "nazli", "name": "Nazli", "role": "Chief Technology Officer", "accent": "Turkish"},
            {"id": "charles", "name": "Charles", "role": "Senior Legal Regulatory Counsel", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c1_data_sovereignty.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "charles",
                "start_ms": 0,
                "end_ms": 7500,
                "text_en": "Nazli, European data protection regulators are escalating scrutiny regarding cross-border telemetry transfers. Standard Contractual Clauses are no longer sufficient without supplementary technical measures.",
                "text_tr": "Nazli, Avrupalı veri koruma düzenleyicileri sınırlar ötesi telemetri aktarımlarına yönelik incelemelerini sıkılaştırıyor. Tamamlayıcı teknik önlemler olmaksızın Standart Sözleşme Maddeleri artık yeterli değil."
            },
            {
                "index": 2,
                "speaker_id": "nazli",
                "start_ms": 7900,
                "end_ms": 17200,
                "text_en": "We anticipated this regulatory shift under the Schrems II ruling, Charles. Our engineering team has been evaluating client-side envelope encryption with sovereign customer-managed keys.",
                "text_tr": "Schrems II kararı kapsamında bu düzenleyici değişimi öngörmüştük Charles. Mühendislik ekibimiz egemen müşteri yönetimli anahtarlarla istemci tarafı zarf şifrelemesini değerlendiriyordu."
            },
            {
                "index": 3,
                "speaker_id": "charles",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "Does the technical design guarantee that US-based cloud hosting providers cannot access plaintext data, even when compelled by sovereign foreign subpoenas?",
                "text_tr": "Teknik tasarım, egemen yabancı mahkeme celpleriyle zorlansalar dahi ABD merkezli bulut barındırma sağlayıcılarının düz metin verilere erişemeyeceğini garanti ediyor mu?"
            },
            {
                "index": 4,
                "speaker_id": "nazli",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "Yes. Master cryptographic key encryption keys (KEKs) reside exclusively in European hardware security modules (HSMs) managed by local European escrow entities. Cloud hyper-scalers hold only cipher-text.",
                "text_tr": "Evet. Ana kriptografik anahtar şifreleme anahtarları (KEK'ler), yalnızca yerel Avrupalı yediemin kuruluşlarca yönetilen Avrupa donanım güvenlik modüllerinde (HSM) bulunur. Bulut devleri yalnızca şifreli metni tutar."
            },
            {
                "index": 5,
                "speaker_id": "charles",
                "start_ms": 35900,
                "end_ms": 44800,
                "text_en": "What impact does this architectural segregation exert upon global database search indexing and server-side aggregations?",
                "text_tr": "Bu mimari ayrıştırma küresel veritabanı arama indekslemesi ve sunucu tarafı veri toplamaları üzerinde nasıl bir etki yaratıyor?"
            },
            {
                "index": 6,
                "speaker_id": "nazli",
                "start_ms": 45200,
                "end_ms": 54500,
                "text_en": "We deployed blind index tokenization and deterministic field hashing for searchable attributes. It incurs a twelve percent compute overhead but preserves absolute sovereign data confidentiality.",
                "text_tr": "Aranabilir nitelikler için kör dizin belirteçleştirmesi ve deterministik alan özetlemesi uyguladık. Yüzde on ikilik bir bilişim maliyeti getiriyor ancak mutlak egemen veri gizliliğini koruyor."
            }
        ],
        "key_vocabulary": [
            {
                "word": "substantiate",
                "vocab_id": "vocab.substantiate",
                "context_note_tr": "Yasal denetimlerde teknik veri güvenliği önlemlerini kanıtlamak."
            },
            {
                "word": "vulnerability",
                "vocab_id": "vocab.vulnerability",
                "context_note_tr": "Sınırlar ötesi veri transferlerindeki düzenleyici ve hukuki zafiyetler."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_sov_01",
                "What legal ruling accelerated regulatory scrutiny over cross-border cloud data transfers?",
                "Sınırlar ötesi bulut veri aktarımlarına yönelik düzenleyici incelemeyi hangi yasal karar hızlandırdı?",
                "The European Schrems II judicial ruling",
                [
                    "The Digital Millennium Copyright Act of 1998",
                    "The United States Declaration of Independence",
                    "The Kyoto Protocol on Global Warming"
                ],
                "Nazli notes: 'We anticipated this regulatory shift under the Schrems II ruling, Charles.'",
                "Nazli Schrems II kararı kapsamındaki düzenleyici değişimi öngördüklerini belirtir."
            ),
            build_q(
                "q_c1_sov_02",
                "Where do the master cryptographic key encryption keys (KEKs) reside?",
                "Ana kriptografik anahtar şifreleme anahtarları (KEK'ler) nerede bulunmaktadır?",
                "Exclusively in European Hardware Security Modules (HSMs) managed by local European escrow entities",
                [
                    "On an unencrypted USB flash drive in the London office reception",
                    "Stored in a shared public Google Docs document",
                    "Inside the physical computer servers of the US cloud provider"
                ],
                "Nazli specifies that KEKs reside exclusively in European HSMs managed by local escrow entities.",
                "Nazli anahtarların Avrupalı bağımsız kuruluşlarca yönetilen Avrupa HSM'lerinde durduğunu belirtir."
            ),
            build_q(
                "q_c1_sov_03",
                "What data format is held by the US-based cloud hyper-scalers?",
                "ABD merkezli bulut devleri veriyi hangi biçimde tutmaktadır?",
                "Only encrypted ciphertext without access to decryption keys",
                [
                    "Plaintext customer names and passwords",
                    "Unencrypted credit card numbers and bank statements",
                    "Physical paper files in cardboard boxes"
                ],
                "Nazli highlights: 'Cloud hyper-scalers hold only cipher-text.'",
                "Nazli bulut sağlayıcılarının yalnızca şifreli metni (ciphertext) tuttuğunu vurgular."
            ),
            build_q(
                "q_c1_sov_04",
                "How does the engineering team enable database searching over encrypted fields?",
                "Mühendislik ekibi şifrelenmiş alanlar üzerinde veritabanı aramasını nasıl mümkün kılmaktadır?",
                "By deploying blind index tokenization and deterministic field hashing",
                [
                    "By decrypting the entire database every morning at six a.m.",
                    "By sending database queries to external search engines",
                    "By printing index cards for every record"
                ],
                "Nazli explains they deployed blind index tokenization and deterministic field hashing.",
                "Nazli kör dizin belirteçleştirmesi ve deterministik özetleme kullandıklarını açıklar."
            ),
            build_q(
                "q_c1_sov_05",
                "What compute overhead is incurred by this client-side encryption architecture?",
                "Bu istemci tarafı şifreleme mimarisi ne kadarlık bir bilişim maliyeti getirmektedir?",
                "A twelve percent compute overhead",
                [
                    "Ninety-five percent overhead",
                    "Zero percent overhead",
                    "Over three hundred percent overhead"
                ],
                "Nazli reports: 'It incurs a twelve percent compute overhead but preserves absolute sovereign data confidentiality.'",
                "Nazli %12'lik bir bilişim maliyeti getirdiğini belirtir."
            )
        ],
        "topic_tags": ["data-sovereignty", "schrems-ii", "encryption", "gdpr-compliance", "c1-mastery"],
        "related_ids": ["vocab.substantiate", "vocab.vulnerability"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.kubernetes-multi-tenant-cluster-consolidation",
        "title": "Cloud Platform: Multi-Tenant Kubernetes Consolidation vs Cluster Sprawl",
        "cefr_level": "C1",
        "category": "engineering_meeting",
        "scenario_context": "Caner, principal infrastructure architect, and Beatrice, director of platform engineering, evaluate consolidating eighty single-tenant Kubernetes clusters into five large multi-tenant clusters.",
        "speakers": [
            {"id": "caner", "name": "Caner", "role": "Principal Infrastructure Architect", "accent": "Turkish"},
            {"id": "beatrice", "name": "Beatrice", "role": "Director of Platform Engineering", "accent": "American"}
        ],
        "audio_ref": "audio/listening/c1_k8s_consolidation.mp3",
        "duration_seconds": 54,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "beatrice",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Caner, our platform team is drowning in operational maintenance overhead managing eighty separate single-tenant EKS Kubernetes clusters.",
                "text_tr": "Caner, platform ekibimiz seksen ayrı tek kiracılı (single-tenant) EKS Kubernetes kümesini yönetirken operasyonel bakım yükü altında eziliyor."
            },
            {
                "index": 2,
                "speaker_id": "caner",
                "start_ms": 7600,
                "end_ms": 16800,
                "text_en": "Cluster sprawl is devastating our control-plane budget and engineering focus. I propose consolidating those eighty clusters into five regional multi-tenant clusters.",
                "text_tr": "Küme saçılması (cluster sprawl) kontrol düzlemi bütçemizi ve mühendislik odağımızı mahvediyor. O seksen kümeyi beş bölgesel çok kiracılı kümede birleştirmeyi öneriyorum."
            },
            {
                "index": 3,
                "speaker_id": "beatrice",
                "start_ms": 17200,
                "end_ms": 25500,
                "text_en": "Multi-tenancy introduces profound noisy-neighbor and security blast-radius risks. How do you prevent a runaway batch job from starving latency-sensitive microservices?",
                "text_tr": "Çok kiracılık derin 'gürültücü komşu' ve güvenlik etki alanı riskleri doğurur. Kontrolden çıkmış bir toplu işin gecikmeye duyarlı mikro servisleri aç bırakmasını nasıl engelleyeceksin?"
            },
            {
                "index": 4,
                "speaker_id": "caner",
                "start_ms": 25900,
                "end_ms": 34800,
                "text_en": "We will enforce hard ResourceQuotas, LimitRanges, and PriorityClasses across every tenant namespace, backed by Karpenter for dynamic right-sized node provisioning.",
                "text_tr": "Dinamik doğru boyutlu düğüm sağlama için Karpenter tarafından desteklenen, her kiracı ad alanı genelinde katı ResourceQuota, LimitRange ve PriorityClass kuralları uygulayacağız."
            },
            {
                "index": 5,
                "speaker_id": "beatrice",
                "start_ms": 35200,
                "end_ms": 44000,
                "text_en": "What about network-level isolation? If a container in the marketing namespace is compromised, what prevents lateral packet sniffing?",
                "text_tr": "Peki ya ağ düzeyinde yalıtım? Pazarlama ad alanındaki bir konteyner ele geçirilirse yanal paket koklamayı ne engelleyecek?"
            },
            {
                "index": 6,
                "speaker_id": "caner",
                "start_ms": 44400,
                "end_ms": 53500,
                "text_en": "We will deploy Cilium CNI with eBPF-enforced network security policies. It enforces Layer 7 protocol filtering and encrypts all pod-to-pod traffic via WireGuard at wire speed.",
                "text_tr": "eBPF ile güçlendirilmiş ağ güvenlik politikalarına sahip Cilium CNI dağıtacağız. Katman 7 protokol filtrelemesini uygular ve tüm pod'lar arası trafiği hat hızında WireGuard ile şifreler."
            }
        ],
        "key_vocabulary": [
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_note_tr": "Çok kiracılı küme mimarilerinde kaynak izolasyonu ve operasyonel dayanıklılık."
            },
            {
                "word": "bottleneck",
                "vocab_id": "vocab.bottleneck",
                "context_note_tr": "Kontrol düzlemi yönetimi ve küme yayılmasından kaynaklanan bakım darboğazı."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_k8s_01",
                "How many single-tenant Kubernetes clusters is the platform team currently struggling to maintain?",
                "Platform ekibi şu anda bakımında zorlandığı kaç adet tek kiracılı Kubernetes kümesi yönetmektedir?",
                "Eighty separate clusters",
                [
                    "Two clusters",
                    "Five hundred clusters",
                    "Only one cluster"
                ],
                "Beatrice opens by highlighting: 'managing eighty separate single-tenant EKS Kubernetes clusters.'",
                "Beatrice 80 ayrı EKS kümesini yönetmenin yükünü dile getirir."
            ),
            build_q(
                "q_c1_k8s_02",
                "To what number of regional multi-tenant clusters does Caner propose consolidating?",
                "Caner kaç adet bölgesel çok kiracılı kümede birleşmeyi önermektedir?",
                "Five regional multi-tenant clusters",
                [
                    "One thousand micro-clusters",
                    "Eighty-five clusters",
                    "Zero clusters by deleting all containers"
                ],
                "Caner proposes consolidating eighty clusters into five regional multi-tenant clusters.",
                "Caner seksen kümeyi beş bölgesel kümede birleştirmeyi önerir."
            ),
            build_q(
                "q_c1_k8s_03",
                "How will Caner prevent 'noisy neighbor' resource starvation across namespaces?",
                "Caner ad alanları arasında 'gürültücü komşu' kaynak tükenmesini nasıl engelleyecektir?",
                "By enforcing hard ResourceQuotas, LimitRanges, and PriorityClasses backed by Karpenter",
                [
                    "By turning off computers whenever a job runs slowly",
                    "By asking developers not to run software during business hours",
                    "By giving every container unlimited memory without restrictions"
                ],
                "Caner specifies hard ResourceQuotas, LimitRanges, PriorityClasses, and Karpenter provisioning.",
                "Caner ResourceQuota, LimitRange ve PriorityClass kurallarını Karpenter ile uygulayacağını belirtir."
            ),
            build_q(
                "q_c1_k8s_04",
                "What networking CNI plugin will provide eBPF-enforced pod isolation and WireGuard encryption?",
                "Hangi CNI eklentisi eBPF tabanlı pod yalıtımı ve WireGuard şifrelemesi sağlayacaktır?",
                "Cilium CNI",
                [
                    "Standard Linux iptables",
                    "Apache Web Server",
                    "Microsoft Windows Defender"
                ],
                "Caner highlights deploying Cilium CNI with eBPF-enforced network security policies.",
                "Caner eBPF destekli Cilium CNI dağıtacaklarını açıklar."
            ),
            build_q(
                "q_c1_k8s_05",
                "At what network layer does Cilium enforce protocol filtering in this architecture?",
                "Cilium bu mimaride hangi ağ katmanında protokol filtrelemesi uygulamaktadır?",
                "Layer 7 (Application layer)",
                [
                    "Layer 1 (Physical cabling layer)",
                    "Layer 2 (Data link layer only)",
                    "Layer 4 only without application inspection"
                ],
                "Caner explicitly states that Cilium: 'enforces Layer 7 protocol filtering and encrypts all pod-to-pod traffic.'",
                "Caner Katman 7 protokol filtrelemesi uygulandığını açıkça belirtir."
            )
        ],
        "topic_tags": ["kubernetes", "multi-tenancy", "cilium-ebpf", "infrastructure-consolidation", "c1-mastery"],
        "related_ids": ["vocab.resilience", "vocab.bottleneck"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "listening.c1.merger-systems-integration-dilemma",
        "title": "Corporate Strategy: Post-Merger Enterprise Systems Convergence",
        "cefr_level": "C1",
        "category": "stakeholder_alignment",
        "scenario_context": "Ipek, VP of Business Applications, and Leonard, Chief Operating Officer, debate whether to execute a forced migration onto Salesforce or build an abstraction layer following an acquisition.",
        "speakers": [
            {"id": "ipek", "name": "Ipek", "role": "VP of Business Applications", "accent": "Turkish"},
            {"id": "leonard", "name": "Leonard", "role": "Chief Operating Officer", "accent": "British"}
        ],
        "audio_ref": "audio/listening/c1_merger_integration.mp3",
        "duration_seconds": 55,
        "transcript_items": [
            {
                "index": 1,
                "speaker_id": "leonard",
                "start_ms": 0,
                "end_ms": 7200,
                "text_en": "Ipek, following our acquisition of Nordic Fintech, our executive committee wants a unified customer view. They are mandating a six-month migration of all their accounts onto our Salesforce instance.",
                "text_tr": "İpek, Nordic Fintech satın alımımızın ardından yönetim kurulumuz birleşik bir müşteri görünümü istiyor. Tüm hesaplarının altı ay içinde bizim Salesforce örneğimize taşınmasını zorunlu kılıyorlar."
            },
            {
                "index": 2,
                "speaker_id": "ipek",
                "start_ms": 7600,
                "end_ms": 17200,
                "text_en": "Leonard, a forced six-month rip-and-replace migration is a recipe for operational catastrophe. Nordic's core banking workflows are deeply intertwined with their bespoke internal CRM.",
                "text_tr": "Leonard, zoraki altı aylık bir sök-ve-değiştir (rip-and-replace) geçişi operasyonel bir felaket reçetesidir. Nordic'in temel bankacılık iş akışları kendi geliştirdikleri özel CRM'leriyle derinden iç içe geçmiş durumda."
            },
            {
                "index": 3,
                "speaker_id": "leonard",
                "start_ms": 17600,
                "end_ms": 25800,
                "text_en": "Maintaining two parallel enterprise CRM stacks will cost an extra two million dollars annually and prevent unified quarterly revenue reporting.",
                "text_tr": "İki paralel kurumsal CRM yapısını sürdürmek yılda fazladan iki milyon dolara mal olacak ve birleşik üç aylık gelir raporlamasını engelleyecektir."
            },
            {
                "index": 4,
                "speaker_id": "ipek",
                "start_ms": 26200,
                "end_ms": 35500,
                "text_en": "I propose a two-phase compromise: build a real-time event-driven integration layer over Kafka to synchronize customer records, establishing the unified executive view immediately.",
                "text_tr": "İki aşamalı bir uzlaşı öneriyorum: Müşteri kayıtlarını senkronize etmek için Kafka üzerinden gerçek zamanlı olay odaklı bir entegrasyon katmanı inşa edelim ve birleşik yönetim görünümünü derhal kuralım."
            },
            {
                "index": 5,
                "speaker_id": "leonard",
                "start_ms": 35900,
                "end_ms": 44800,
                "text_en": "How does that integration layer protect us from divergent data models and schema conflicts between the two corporate entities?",
                "text_tr": "Bu entegrasyon katmanı bizi iki kurumsal varlık arasındaki farklı veri modellerinden ve şema çakışmalarından nasıl koruyacak?"
            },
            {
                "index": 6,
                "speaker_id": "ipek",
                "start_ms": 45200,
                "end_ms": 54500,
                "text_en": "We will implement an Enterprise Canonical Data Model with bidirectional transformation adapters, allowing their sales teams to maintain commercial momentum while phase two sunsetting begins next year.",
                "text_tr": "Çift yönlü dönüşüm bağdaştırıcılarına sahip bir Kurumsal Standart Veri Modeli (Canonical Data Model) uygulayacağız; bu onların satış ekiplerinin ticari ivmeyi korumasını sağlarken ikinci aşama devreden çıkarma seneye başlayacak."
            }
        ],
        "key_vocabulary": [
            {
                "word": "consensus",
                "vocab_id": "vocab.consensus",
                "context_note_tr": "Şirket birleşmelerinde operasyonel sistem entegrasyonu uzlaşısı."
            },
            {
                "word": "asynchronous",
                "vocab_id": "vocab.asynchronous",
                "context_note_tr": "Kurumsal sistemler arasında Kafka tabanlı asenkron veri senkronizasyonu."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_c1_mrg_01",
                "What aggressive migration timeline was originally demanded by the executive committee?",
                "Yönetim kurulu başlangıçta hangi agresif geçiş takvimini talep etmişti?",
                "A forced six-month migration of all acquired accounts onto Salesforce",
                [
                    "A ten-year gradual transition plan",
                    "A twenty-four hour complete shutdown and manual data entry",
                    "Canceling all existing customer accounts immediately"
                ],
                "Leonard states the committee is mandating a six-month migration onto Salesforce.",
                "Leonard kurulun 6 ay içinde tüm hesapların Salesforce'a taşınmasını zorunlu kıldığını söyler."
            ),
            build_q(
                "q_c1_mrg_02",
                "Why does Ipek warn against a sudden 'rip-and-replace' migration of Nordic Fintech's CRM?",
                "İpek Nordic Fintech'in CRM'inin ani bir 'sök-ve-değiştir' ile taşınmasına karşı neden uyarıda bulunuyor?",
                "Nordic's core banking workflows are deeply intertwined with their bespoke internal CRM",
                [
                    "Nordic employees do not know how to turn on computer monitors",
                    "Salesforce has legally banned all European financial institutions",
                    "Nordic's contracts forbid using any software created in North America"
                ],
                "Ipek warns that Nordic's banking workflows are deeply coupled with their custom CRM.",
                "İpek bankacılık iş akışlarının özel CRM sistemleriyle derinden iç içe geçmiş olduğunu belirtir."
            ),
            build_q(
                "q_c1_mrg_03",
                "What annual cost penalty does Leonard cite for maintaining parallel CRM stacks?",
                "Leonard paralel CRM yapılarını sürdürmenin yıllık hangi maliyet cezasına yol açacağını belirtir?",
                "An extra two million dollars annually",
                [
                    "Fifty thousand dollars",
                    "One hundred million dollars",
                    "Ten dollars per employee"
                ],
                "Leonard highlights: 'Maintaining two parallel enterprise CRM stacks will cost an extra two million dollars annually.'",
                "Leonard paralel yapıların yılda fazladan iki milyon dolara mal olacağını ifade eder."
            ),
            build_q(
                "q_c1_mrg_04",
                "What intermediate solution does Ipek propose to provide unified executive visibility?",
                "İpek birleşik yönetim görünürlüğü sağlamak için hangi ara çözümü önermektedir?",
                "Building a real-time event-driven integration layer over Kafka to synchronize customer records",
                [
                    "Hiring fifty clerks to copy and paste data into Microsoft Excel spreadsheets",
                    "Deleting all customer records from both databases to start fresh",
                    "Conducting weekly telephone calls to read customer account balances out loud"
                ],
                "Ipek proposes building a real-time event-driven integration layer over Kafka.",
                "İpek Kafka üzerinden gerçek zamanlı olay odaklı bir entegrasyon katmanı kurmayı önerir."
            ),
            build_q(
                "q_c1_mrg_05",
                "How will data model schema conflicts between the two corporate entities be reconciled?",
                "İki kurumsal varlık arasındaki veri modeli şema çakışmaları nasıl uzlaştırılacaktır?",
                "Through an Enterprise Canonical Data Model with bidirectional transformation adapters",
                [
                    "By letting the software flip a random coin to decide which schema wins",
                    "By forcing all employees to memorize binary machine code",
                    "By converting all database text fields into uncompressed audio recordings"
                ],
                "Ipek explains they will implement an Enterprise Canonical Data Model with bidirectional transformation adapters.",
                "İpek çift yönlü adaptörlere sahip Kurumsal Standart Veri Modeli uygulayacaklarını açıklar."
            )
        ],
        "topic_tags": ["enterprise-architecture", "m-and-a-integration", "crm-migration", "data-contracts", "c1-mastery"],
        "related_ids": ["vocab.consensus", "vocab.asynchronous"],
        "status": "APPROVED",
        "version": 1
    }
]

if __name__ == "__main__":
    print(f"Generated {len(C1_LISTENING_SCENARIOS)} C1 listening scenarios.")
    for s in C1_LISTENING_SCENARIOS:
        print(f"  [{s['cefr_level']}] {s['id']} - {s['title']} ({len(s['transcript_items'])} items, {len(s['comprehension_questions'])} questions)")
